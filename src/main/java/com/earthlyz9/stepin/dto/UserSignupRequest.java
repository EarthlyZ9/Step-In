package com.earthlyz9.stepin.dto;

import com.earthlyz9.stepin.entities.User;
import com.earthlyz9.stepin.entities.UserRole;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserSignupRequest {
    @Size(max = 100)
    private String email;

    @Size(max = 50)
    private String nickname;
    private String password;
    private String confirmPassword;

    public User toEntity(String encodedPassword) {
        return User.builder()
            .email(this.email)
            .password(encodedPassword)
            .nickname(this.nickname)
            .role(UserRole.USER)
            .build();
    }
}
