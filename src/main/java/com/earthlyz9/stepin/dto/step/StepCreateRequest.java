package com.earthlyz9.stepin.dto.step;

import com.earthlyz9.stepin.entities.Step;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "스텝 생성을 위한 스키마")
public class StepCreateRequest {

    @JsonIgnore
    private Integer id;
    @JsonIgnore
    private Integer projectId;

    @JsonIgnore
    private Integer ownerId;

    @JsonIgnore
    private Integer number;

    @Schema(description = "스텝 이름")
    @NotNull(message = "name is required")
    @NotBlank(message = "name should not be empty string")
    private String name;

    public static Step toEntity(StepCreateRequest data) {
        return Step.builder()
            .id(data.getId())
            .name(data.getName())
            .ownerId(data.getOwnerId())
            .projectId(data.getProjectId())
            .number(data.getNumber())
            .build();
    }
}
