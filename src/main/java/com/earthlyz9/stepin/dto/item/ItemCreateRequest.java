package com.earthlyz9.stepin.dto.item;

import com.earthlyz9.stepin.entities.Item;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "아이탬 생성을 위한 스키마")
public class ItemCreateRequest {
    @JsonIgnore
    private Integer id;
    @JsonIgnore
    private Integer stepId;

    @JsonIgnore
    private Integer ownerId;

    @Schema(description = "아이템 내용")
    private String content;

    @Schema(description = "이전 스텝의 부모 아이템")
    private int parentItemId;

    public static Item toEntity(ItemCreateRequest data) {
        return Item.builder()
            .id(data.getId())
            .stepId(data.getStepId())
            .ownerId(data.getOwnerId())
            .content(data.getContent())
            .parentItemId(data.getParentItemId())
            .build();
    }
}
