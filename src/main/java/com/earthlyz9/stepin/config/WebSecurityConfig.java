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

//    @Bean
//    public UserDetailsManager userDetailsManager(DataSource dataSource) {
//        JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
//
//        // query users
//        jdbcUserDetailsManager.setUsersByUsernameQuery("select username, password, is_active from user where username=?");
//
//        // query roles
//        jdbcUserDetailsManager.setAuthoritiesByUsernameQuery("select username, role from user where username=?");
//
//        return jdbcUserDetailsManager;
//    }

    // Restricting access based on roles
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(configurer ->
            configurer
                .requestMatchers(HttpMethod.GET, "/**").permitAll()
        );

        // use HTTP Basic authentication
        http.httpBasic();

        // disable CSRF
        http.csrf().disable();

        return http.build();
    }
}
