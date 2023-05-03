package com.earthlyz9.stepin.jwt.filter;

import com.earthlyz9.stepin.utils.PasswordUtils;
import com.earthlyz9.stepin.entities.User;
import com.earthlyz9.stepin.jwt.service.JwtService;
import com.earthlyz9.stepin.services.UserServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {

    private static final String BYPASS_URL_PATTERN_PREFIX = "/api/auth";
    private static final List<String> BYPASS_URL_PATTERN = List.of("/basic/login", "/basic/sign-up");

    private final JwtService jwtService;
    private final UserServiceImpl userServiceImpl;

    private final GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        String requestUri = request.getRequestURI();
        System.out.println(requestUri + " jwt filter");

        // Bypass authentication filter
        if (requestUri.contains(BYPASS_URL_PATTERN_PREFIX)) {
            for (String urlPattern : BYPASS_URL_PATTERN) {
                if (requestUri.contains(BYPASS_URL_PATTERN_PREFIX + urlPattern)) {
                    System.out.println("bypass");
                    filterChain.doFilter(request, response);
                    return;
                }
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
            } filterChain.doFilter(request, response);
        } else {
            if (accessToken != null) {
                // refresh token invalid + access token valid
                Optional<String> email = jwtService.extractEmail(accessToken);
                email.ifPresent(v -> saveAuthentication(userServiceImpl.getUserByEmail(v)));
                filterChain.doFilter(request, response);
            } else filterChain.doFilter(request, response);
        }

    }


    public void saveAuthentication(User user) {
        String password = user.getPassword();

        if (password == null) {
            password = PasswordUtils.generateRandomPassword(8);
        }

        UserDetails userDetailsUser = org.springframework.security.core.userdetails.User.builder()
            .username(user.getEmail())
            .password(password)
            .roles(user.getRole().name())
            .build();

        Authentication authentication =
            new UsernamePasswordAuthenticationToken(userDetailsUser, null,
                authoritiesMapper.mapAuthorities(userDetailsUser.getAuthorities()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
