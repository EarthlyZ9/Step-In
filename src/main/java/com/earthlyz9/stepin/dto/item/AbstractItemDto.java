package com.earthlyz9.stepin.dto.item;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Setter
public abstract class AbstractItemDto extends RepresentationModel<AbstractItemDto> {
    @JsonProperty(access = Access.READ_ONLY)
    protected Integer id;

    @NotNull(message = "content is required")
    @NotBlank(message = "content should not be empty string")
    @Schema(description = "아이템 설명")
    protected String content;

    @Length(max = 500, message = "item memo cannot exceed 500 characters")
    @Schema(description = "항목에 대한 메모")
    protected String memo;

    @JsonProperty(access = Access.READ_ONLY)
    protected Integer ownerId;

    @JsonProperty(access = Access.READ_ONLY)
    protected Date createdAt;

    @JsonProperty(access = Access.READ_ONLY)
    protected Date updatedAt;
}
