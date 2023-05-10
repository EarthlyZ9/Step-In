package com.earthlyz9.stepin.dto.item;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "아이탬 생성을 위한 스키마")
public class ItemCreateRequest {

    @Schema(description = "아이템 내용")
    private String content;
}
