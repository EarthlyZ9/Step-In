package com.earthlyz9.stepin.config;

import com.earthlyz9.stepin.auth.CustomAccessDeniedHandler;
import com.earthlyz9.stepin.auth.CustomAuthenticationEntryPoint;
import com.earthlyz9.stepin.auth.filter.JsonUsernamePasswordAuthenticationFilter;
import com.earthlyz9.stepin.auth.handler.LoginFailureHandler;
import com.earthlyz9.stepin.auth.handler.LoginSuccessHandler;
import com.earthlyz9.stepin.auth.service.LoginService;
import com.earthlyz9.stepin.jwt.filter.JwtAuthenticationProcessingFilter;
import com.earthlyz9.stepin.jwt.service.JwtService;
import com.earthlyz9.stepin.services.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final LoginService loginService;
    private final JwtService jwtService;
    private final UserServiceImpl userServiceImpl;
    private final ObjectMapper objectMapper;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Restricting access based on roles
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.httpBasic().disable()
            .formLogin().disable()
            .csrf().disable()
            .headers().frameOptions().disable()
            .and()

            // No session
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .cors()
            .and()

            // Url Patterns
            .authorizeHttpRequests()
                .requestMatchers(HttpMethod.GET, "/actuator/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/auth/basic/sign-up").permitAll()
                .requestMatchers(HttpMethod.POST, "/auth/token/refresh").permitAll()
                .requestMatchers(HttpMethod.POST, "/auth/guest/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/auth/oauth2/callback").permitAll()
                .anyRequest().authenticated();

        http.exceptionHandling()
            .authenticationEntryPoint(customAuthenticationEntryPoint)
            .accessDeniedHandler(customAccessDeniedHandler);

        // 순서 : LogoutFilter -> JwtAuthenticationProcessingFilter -> CustomJsonUsernamePasswordAuthenticationFilter
        http.addFilterAfter(new JwtAuthenticationProcessingFilter(jwtService, userServiceImpl), LogoutFilter.class);
        http.addFilterAfter(jsonUsernamePasswordAuthenticationFilter(), JwtAuthenticationProcessingFilter.class);

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/swagger-ui/**", "/v3/api-docs/**");
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        configuration.setAllowedMethods(Arrays.asList("GET","POST", "DELETE", "PATCH", "OPTIONS"));
        configuration.setExposedHeaders(Arrays.asList("Set-Cookie", "Authorization"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * 로그인 성공 시 호출되는 LoginSuccessJWTProviderHandler 빈 등록
     */
    @Bean
    public LoginSuccessHandler loginSuccessHandler() {
        return new LoginSuccessHandler(jwtService, userServiceImpl);
    }

    /**
     * 로그인 실패 시 호출되는 LoginFailureHandler 빈 등록
     */
    @Bean
    public LoginFailureHandler loginFailureHandler() {
        return new LoginFailureHandler();
    }

    /**
     * AuthenticationManager 설정 후 등록 PasswordEncoder를 사용하는 AuthenticationProvider 지정
     * (PasswordEncoder는 위에서 등록한 PasswordEncoder 사용) FormLogin(기존 스프링 시큐리티 로그인)과 동일하게
     * DaoAuthenticationProvider 사용 UserDetailsService는 커스텀 LoginService로 등록 또한, FormLogin과 동일하게
     * AuthenticationManager로는 구현체인 ProviderManager 사용(return ProviderManager)
     */
    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(loginService);
        return new ProviderManager(provider);
    }

    /**
     * CustomJsonUsernamePasswordAuthenticationFilter 빈 등록 커스텀 필터를 사용하기 위해 만든 커스텀 필터를 Bean으로 등록
     * setAuthenticationManager(authenticationManager())로 위에서 등록한
     * AuthenticationManager(ProviderManager) 설정 로그인 성공 시 호출할 handler, 실패 시 호출할 handler로 위에서 등록한
     * handler 설정
     */
    @Bean
    public JsonUsernamePasswordAuthenticationFilter jsonUsernamePasswordAuthenticationFilter() {
        JsonUsernamePasswordAuthenticationFilter jsonUsernamePasswordLoginFilter
            = new JsonUsernamePasswordAuthenticationFilter(objectMapper);
        jsonUsernamePasswordLoginFilter.setAuthenticationManager(authenticationManager());
        jsonUsernamePasswordLoginFilter.setAuthenticationSuccessHandler(loginSuccessHandler());
        jsonUsernamePasswordLoginFilter.setAuthenticationFailureHandler(loginFailureHandler());
        return jsonUsernamePasswordLoginFilter;
    }
}
