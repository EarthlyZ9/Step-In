package com.earthlyz9.stepin.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserRole {
    USER("ROLE_USER"),
    GUEST("ROLE_GUEST");

    private final String key;
}
