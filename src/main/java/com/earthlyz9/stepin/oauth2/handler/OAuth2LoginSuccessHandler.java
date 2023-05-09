package com.earthlyz9.stepin.oauth2.handler;

import com.earthlyz9.stepin.entities.User;
import com.earthlyz9.stepin.jwt.service.JwtService;
import com.earthlyz9.stepin.oauth2.CustomOAuth2User;
import com.earthlyz9.stepin.services.UserServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final UserServiceImpl userServiceImpl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException {

        try {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
            loginSuccess(response, oAuth2User);
        } catch (Exception e) {
            throw e;
        }

    }

    private void loginSuccess(HttpServletResponse response, CustomOAuth2User oAuth2User)
        throws IOException {
        System.out.println("성공");
        String email = oAuth2User.getEmail();
        User user = userServiceImpl.getUserByEmail(email);

        String accessToken = jwtService.createAccessToken(email);
        String refreshToken = jwtService.createRefreshToken(email);

        jwtService.setRefreshTokenCookie(response, refreshToken);

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> userWithAccessToken = objectMapper.convertValue(user, new TypeReference<>() {});
        userWithAccessToken.put("accessToken", accessToken);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        response.getWriter().write(objectMapper.writeValueAsString(userWithAccessToken));
    }
}
