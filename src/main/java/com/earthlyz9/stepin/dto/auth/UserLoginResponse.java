package com.earthlyz9.stepin.dto.auth;

import com.earthlyz9.stepin.entities.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Schema(description = "access token 이 포함된 유저 정보")
@Getter
public class UserLoginResponse extends User {
    @Schema(description = "액세스 토큰")
    private String accessToken;
}
