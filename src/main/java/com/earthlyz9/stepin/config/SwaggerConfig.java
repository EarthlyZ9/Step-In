package com.earthlyz9.stepin.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import jakarta.persistence.Entity;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Service;

@OpenAPIDefinition(
    info = @Info(
        title = "spring commerce API 명세서",
        description = "API 명세서",
        version = "v1",
        contact = @Contact(
            name = "beginners",
            email = "sample@email.co.kr"
        )
    )
)
// TODO: SecuritySchemes
@Configuration
public class SwaggerConfig {
//
//    @Bean
//    public GroupedOpenApi group1() {
//        return GroupedOpenApi.builder()
//            .group("step-in")
//            .pathsToMatch("/group1/**")
//            // .packagesToScan("com.example.swagger") // package 필터 설정
//            .build();

}