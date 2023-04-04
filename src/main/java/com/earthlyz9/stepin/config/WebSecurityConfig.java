package com.earthlyz9.stepin.config;

import javax.sql.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public UserDetailsManager userDetailsManager(DataSource dataSource) {
        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);

        // query users
        jdbcUserDetailsManager.setUsersByUsernameQuery("select email, password, is_active from user where email=?");

        // query roles
        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery("select email, role from user where email=?");

        return jdbcUserDetailsManager;
    }

    // Restricting access based on roles
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(configurer ->
            configurer
                .requestMatchers(HttpMethod.GET, "/**").permitAll()
                .requestMatchers(HttpMethod.POST, "**").hasRole("USER")
                .requestMatchers(HttpMethod.PATCH, "**").hasRole("USER")
                .requestMatchers(HttpMethod.DELETE, "**").hasRole("USER")
        );

        // use HTTP Basic authentication
        http.httpBasic();

        // disable CSRF
        http.csrf().disable();

        return http.build();
    }
}
