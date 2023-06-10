package com.earthlyz9.stepin.controllers;


import com.earthlyz9.stepin.dto.auth.AccessTokenResponse;
import com.earthlyz9.stepin.dto.auth.UserLoginRequest;
import com.earthlyz9.stepin.dto.auth.UserLoginResponse;
import com.earthlyz9.stepin.dto.auth.OAuth2CallbackRequest;
import com.earthlyz9.stepin.entities.SocialProviderType;
import com.earthlyz9.stepin.entities.User;
import com.earthlyz9.stepin.dto.auth.UserSignupRequest;
import com.earthlyz9.stepin.entities.UserRole;
import com.earthlyz9.stepin.exceptions.AuthenticationFailedException;
import com.earthlyz9.stepin.exceptions.ExceptionResponse;
import com.earthlyz9.stepin.exceptions.PermissionDeniedException;
import com.earthlyz9.stepin.exceptions.ValidationError;
import com.earthlyz9.stepin.jwt.service.JwtService;
import com.earthlyz9.stepin.oauth2.service.OAuth2Service;
import com.earthlyz9.stepin.services.UserServiceImpl;
import com.earthlyz9.stepin.utils.AuthUtils;
import com.earthlyz9.stepin.utils.CookieUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/auth")
@Tag(name = "Auth", description = "인증과 관련된 동작")
public class AuthController {
    private final UserServiceImpl userServiceImpl;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    private final ClientRegistrationRepository clientRegistrationRepository;

    @Value("${jwt.refresh.name}")
    private String refreshName;

    @Value("${jwt.refresh.expiration}")
    private int refreshExpiry;

    @Autowired
    public AuthController(UserServiceImpl userServiceImpl, PasswordEncoder passwordEncoder, JwtService jwtService, ClientRegistrationRepository clientRegistrationRepository) {
        this.userServiceImpl = userServiceImpl;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.clientRegistrationRepository = clientRegistrationRepository;
    }

    @Operation(
        summary = "이메일 주소를 이용하여 회원가입을 합니다",
        responses = {
            @ApiResponse(description = "created", responseCode = "201", content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)))
        }
    )
    @PostMapping("/basic/sign-up")
    public ResponseEntity<User> basicSignUp(@RequestBody @Valid UserSignupRequest userSignupRequest) throws ValidationError {
        if (!userSignupRequest.getPassword().equals(userSignupRequest.getConfirmPassword()))
            throw new ValidationError("Password does not match confirmation password");

        String encodedPassword = passwordEncoder.encode(userSignupRequest.getPassword());

        User newUser = userSignupRequest.toEntity(encodedPassword);

        User savedUser = userServiceImpl.createBasicUser(newUser);

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/auth/me").buildAndExpand("/auth/me").toUri();
        return ResponseEntity.created(location).body(savedUser);
    }

    @Operation(
        summary = "로그아웃",
        responses = {
            @ApiResponse(description = "success", responseCode = "204")
        }
    )
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        CookieUtils.deleteCookie(request, response, refreshName);
        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "리프레쉬 토큰을 이용하여 액세스 토큰을 재발급 받습니다",
        responses = {
            @ApiResponse(description = "success", responseCode = "200", content = @Content(schema = @Schema(implementation = AccessTokenResponse.class))),
            @ApiResponse(description = "refresh token invalid", responseCode = "401", content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
        }
    )
    @PostMapping("/token/refresh")
    public ResponseEntity<AccessTokenResponse> refresh(HttpServletRequest request) {
        Optional<String> refreshToken = jwtService.extractRefreshToken(request);
        String validRefreshToken = refreshToken.filter(jwtService::isTokenValid).orElse(null);
        if (validRefreshToken == null) throw new AuthenticationFailedException();

        String email = String.valueOf(jwtService.extractEmail(validRefreshToken));

        String newAccessToken = jwtService.createAccessToken(email);
        String newRefreshToken = jwtService.createRefreshToken(email);

        AccessTokenResponse responseBody = new AccessTokenResponse(newAccessToken);

        ResponseCookie responseCookie = ResponseCookie.from(refreshName, newRefreshToken)
            .httpOnly(false)
            .secure(false)
            .path("/")
            .maxAge(refreshExpiry / 1000) // 2 weeks
            .build();
        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
            .body(responseBody);
    }

    @Operation(
        summary = "현재 로그인한 사용자의 정보를 가져옵니다",
        responses = {
            @ApiResponse(description = "success", responseCode = "200", content = @Content(schema = @Schema(implementation = User.class)))
        }
    )
    @SecurityRequirement(name = "Bearer Token")
    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser() {
        String email = AuthUtils.getRequestUserName();
        User currentUser = userServiceImpl.getUserByEmail(email);
        return ResponseEntity.ok(currentUser);
    }

    /**
     * For openapi swagger documentation
     */
    @Operation(
        summary = "일반 로그인",
        responses = {
            @ApiResponse(description = "success", responseCode = "200", content = @Content(schema = @Schema(implementation = UserLoginResponse.class))),
            @ApiResponse(description = "Authentication failed", responseCode = "401", content = @Content(schema = @Schema(implementation = ExceptionResponse.class)))
        }
    )
    @PostMapping("/basic/login")
    public ResponseEntity<User> login(@RequestBody @Valid UserLoginRequest userLoginRequest) {
        return null;
    }

    @Operation(
        summary = "게스트 로그인",
        responses = {
            @ApiResponse(description = "success", responseCode = "201", content = @Content(schema = @Schema(implementation = UserLoginResponse.class))),
        }
    )
    @PostMapping("/guest/login")
    public ResponseEntity<Map<String, Object>> guestLogin() {
        User savedUser = userServiceImpl.createGuestUser();

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/auth/me").buildAndExpand("/auth/me").toUri();
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> userWithAccessToken = objectMapper.convertValue(savedUser, new TypeReference<>() {});
        System.out.println(savedUser.getEmail());
        userWithAccessToken.put("accessToken", jwtService.createAccessToken(savedUser.getEmail()));
        return ResponseEntity.created(location).body(userWithAccessToken);
    }


    @Operation(
        summary = "게스트 유저를 삭제합니다",
        description = "브라우저를 종료하면 로그인한 게스트의 모든 정보는 삭제됩니다",
        responses = {
            @ApiResponse(description = "deleted", responseCode = "204")
        }
    )
    @DeleteMapping("/guest/logout")
    @SecurityRequirement(name = "Bearer Token")
    public ResponseEntity<Void> guestLogout(HttpServletRequest request, HttpServletResponse response) {
        CookieUtils.deleteCookie(request, response, refreshName);
        int guestUserId = AuthUtils.getRequestUserId();
        if (AuthUtils.getRequestUser().getRole().equals(UserRole.GUEST)) userServiceImpl.deleteGuestUserById(guestUserId);
        else throw new PermissionDeniedException("only guests allowed");

        return ResponseEntity.noContent().build();
    }

    @Operation(
        summary = "OAuth2 Authorization Code 를 이용해 소셜 유저를 생성합니다",
        responses = {
            @ApiResponse(description = "success", responseCode = "201", content = @Content(schema = @Schema(implementation = UserLoginResponse.class))),
        }
    )
    @PostMapping("/oauth2/callback")
    public ResponseEntity<UserLoginResponse> OAuth2Callback(@Valid @RequestBody
    OAuth2CallbackRequest body) {
        String registrationId = body.getRegistrationId();
        List<String> choices = Arrays.asList("KAKAO", "NAVER", "GOOGLE");

        if (!choices.contains(registrationId.toUpperCase())) {
            throw new ValidationError("invalid registration id");
        }

        ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId(
            registrationId);

        OAuth2Service service = new OAuth2Service(clientRegistration, userServiceImpl);

        String oAuthAccessToken = service.obtainAccessToken(body.getCode());

        User user = service.getUser(oAuthAccessToken);

        URI location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/auth/me").buildAndExpand("/auth/me").toUri();
        UserLoginResponse dto = UserLoginResponse.toDto(user, jwtService.createAccessToken(user.getEmail()));

        return ResponseEntity.created(location).body(dto);
    }

    @PostMapping("/oauth2/test")
    public String oAuthTest(@Valid @RequestBody OAuth2CallbackRequest body) {
        if (!Arrays.asList(SocialProviderType.values()).contains(body.getRegistrationId().toUpperCase())) {
            throw new ValidationError("invalid registration id");
        }
        return body.getRegistrationId();
    }
}
