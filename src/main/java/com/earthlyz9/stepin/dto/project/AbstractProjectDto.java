package com.earthlyz9.stepin.dto.project;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@Setter
@Getter
public abstract class AbstractProjectDto extends RepresentationModel<AbstractProjectDto> {
    @JsonProperty(access = Access.READ_ONLY)
    protected Integer id;

    @Schema(description = "프로젝트 이름")
    @NotNull(message = "name is required")
    @NotBlank(message = "name should not be empty string")
    protected String name;

    @JsonProperty(access = Access.READ_ONLY)
    protected LocalDateTime createdAt;

    @JsonProperty(access = Access.READ_ONLY)
    protected LocalDateTime updatedAt;
}
