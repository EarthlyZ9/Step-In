package com.earthlyz9.stepin.dto.project;

import com.earthlyz9.stepin.entities.Project;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@Schema(description = "프로젝트 생성을 위한 스키마")
public class ProjectCreateRequest {
    @JsonProperty(access = Access.WRITE_ONLY)
    private Integer id;

    @JsonProperty(access = Access.WRITE_ONLY)
    private Integer ownerId;

    @NotNull(message = "name is required")
    @NotBlank(message = "name should not be empty string")
    @Schema(description = "프로젝트 이름")
    private String name;

    public static Project toEntity(ProjectCreateRequest data) {
        return Project.builder()
            .id(data.getId())
            .name(data.getName())
            .ownerId(data.getOwnerId())
            .build();
    }
}
