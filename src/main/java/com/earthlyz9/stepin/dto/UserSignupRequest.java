package com.earthlyz9.stepin.dto;

import com.earthlyz9.stepin.entities.User;
import com.earthlyz9.stepin.entities.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    @NotBlank
    @Schema(description = "이메일", example = "example@email.com")
    @Email
    private String email;

    @Size(max = 50)
    @NotBlank
    @NotNull
    @Schema(description = "사용할 이름", example = "지구")
    private String nickname;

    @NotBlank
    @NotNull
    @Schema(description = "비밀번호", example = "password")
    private String password;

    @NotBlank
    @NotNull
    @Schema(description = "비밀번호 재입력", example = "password")
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
