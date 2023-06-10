package com.earthlyz9.stepin.dto.auth;

import com.earthlyz9.stepin.entities.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "access token 이 포함된 유저 정보")
@Getter
@Setter
@NoArgsConstructor
public class UserLoginResponse extends User {

    @Schema(description = "액세스 토큰")
    private String accessToken;

    public static UserLoginResponse toDto(User entity, String accessToken) {
        UserLoginResponse dto = new UserLoginResponse();
        dto.setId(entity.getId());
        dto.setNickname(entity.getNickname());
        dto.setEmail(entity.getEmail());
        dto.setImageUrl(entity.getImageUrl());
        dto.setIsActive(entity.getIsActive());
        dto.setSocialProviderType(entity.getSocialProviderType());
        dto.setSocialId(entity.getSocialId());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        dto.setAccessToken(accessToken);
        return dto;
    }
}
