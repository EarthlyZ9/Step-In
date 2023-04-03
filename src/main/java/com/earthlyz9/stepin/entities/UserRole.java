package com.earthlyz9.stepin.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserRole {
    ROLE_USER("ROLE_USER");

    private final String value;
}
