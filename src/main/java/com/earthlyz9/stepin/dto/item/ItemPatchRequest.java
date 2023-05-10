package com.earthlyz9.stepin.dto.item;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
public class ItemPatchRequest {

    @Schema(description = "아이템 내용")
    private String content;

    @Schema(description = "아이템에 대한 메모")
    @Length(max = 500)
    private String memo;
}
