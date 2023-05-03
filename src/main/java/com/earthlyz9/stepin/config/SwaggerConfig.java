package com.earthlyz9.stepin.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
    info = @Info(
        title = "Step In Project",
        description = "A new sketchbook for your API",
        version = "v1",
        contact = @Contact(
            name = "EarthlyZ9",
            email = "earthlyz9.dev@gmail.com"
        )
    )
)
@SecuritySchemes({
    @SecurityScheme(name = "Bearer Token",
        type = SecuritySchemeType.APIKEY,
        description = "Jwt bearer token",
        in = SecuritySchemeIn.HEADER,
        paramName = "Authorization"),
})
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