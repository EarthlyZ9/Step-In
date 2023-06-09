package com.earthlyz9.stepin.jwt.filter;

import com.earthlyz9.stepin.auth.CustomUserDetails;
import com.earthlyz9.stepin.utils.PasswordUtils;
import com.earthlyz9.stepin.entities.User;
import com.earthlyz9.stepin.jwt.service.JwtService;
import com.earthlyz9.stepin.services.UserServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {
    private static final List<String> BYPASS_URL_PATTERN = List.of("/auth/basic/login", "/oauth2", "/auth/token/refresh", "/auth/basic/signup", "/auth/guest/login");

    private final JwtService jwtService;
    private final UserServiceImpl userServiceImpl;

    private final GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        String requestUri = request.getRequestURI();
        System.out.println(requestUri + " jwt filter");

        // Bypass authentication filter
        for (String urlPattern : BYPASS_URL_PATTERN) {
            if (requestUri.contains(urlPattern)) {
                System.out.println("bypass");
                filterChain.doFilter(request, response);
                return;
            }
        }

        String refreshToken = jwtService.extractRefreshToken(request)
            .filter(jwtService::isTokenValid)
            .orElse(null);

        String accessToken = jwtService.extractAccessToken(request)
            .filter(jwtService::isTokenValid)
            .orElse(null);

        if (refreshToken != null) {

            if (accessToken != null) {
                // refresh token valid + access token valid
                Optional<String> email = jwtService.extractEmail(accessToken);
                email.ifPresent(v -> saveAuthentication(userServiceImpl.getUserByEmail(v)));
                filterChain.doFilter(request, response);
            } else {
                // refresh token valid + access token invalid
                response.setContentType("application/json;charset=UTF-8");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"code\": 401,\"message\":\"Invalid credentials. Access token may be invalid or expired\", \"timestamp\": \"" + LocalDateTime.now() + "\"}");
            }
        } else {
            if (accessToken != null) {
                // refresh token invalid + access token valid
                Optional<String> email = jwtService.extractEmail(accessToken);
                email.ifPresent(v -> saveAuthentication(userServiceImpl.getUserByEmail(v)));
                filterChain.doFilter(request, response);
            } else {
                // refresh token invalid + access token invalid
                response.setContentType("application/json;charset=UTF-8");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"code\": 401,\"message\":\"Invalid credentials. Both access token and refresh token are invalid or expired\", \"timestamp\": \"" + LocalDateTime.now() + "\"}");
            }
        }

    }


    public void saveAuthentication(User user) {
        String password = user.getPassword();

        if (password == null) {
            password = PasswordUtils.generateRandomPassword(8);
        }

        CustomUserDetails userDetailsUser = new CustomUserDetails(
            user.getId(),
            user.getEmail(),
            password,
            user.getRole().name(),
            user
        );

        Authentication authentication =
            new UsernamePasswordAuthenticationToken(userDetailsUser, null,
                authoritiesMapper.mapAuthorities(userDetailsUser.getAuthorities()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
