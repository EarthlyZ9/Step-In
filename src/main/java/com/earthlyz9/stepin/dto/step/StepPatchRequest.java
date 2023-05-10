package com.earthlyz9.stepin.dto.step;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "스텝 수정을 위한 스키마")
public class StepPatchRequest {

    @NotNull(message = "name is required")
    @NotBlank(message = "name should not be empty string")
    @Schema(description = "스텝의 이름")
    private String name;
}
