package com.earthlyz9.stepin.dto.step;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
public abstract class AbstractStepDto extends RepresentationModel<AbstractStepDto> {
    @JsonProperty(access = Access.READ_ONLY)
    protected Integer id;

    @NotNull(message = "name is required")
    @NotBlank(message = "name should not be empty string")
    protected String name;

    @Min(value = 1, message = "number should be equal to or greater than 1")
    protected Integer number;

    @JsonProperty(access = Access.READ_ONLY)
    protected Integer ownerId;

    @JsonProperty(access = Access.READ_ONLY)
    protected Date createdAt;

    @JsonProperty(access = Access.READ_ONLY)
    protected Date updatedAt;
}
