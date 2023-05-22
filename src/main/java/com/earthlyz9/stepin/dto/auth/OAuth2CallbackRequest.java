package com.earthlyz9.stepin.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class OAuth2CallbackRequest {
    @NotNull(message = "authorization code is required")
    @NotBlank(message = "authorization code should not be empty string")
    private String code;

    @NotNull(message = "registrationId is required")
    @NotBlank(message = "registrationId should not be empty string")
    private String registrationId;
}

