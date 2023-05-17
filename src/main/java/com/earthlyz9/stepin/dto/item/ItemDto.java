package com.earthlyz9.stepin.dto.item;

import com.earthlyz9.stepin.dto.step.SimpleStepDto;
import com.earthlyz9.stepin.entities.Item;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.server.core.Relation;

@Setter
@Getter
@Relation(collectionRelation = "items", itemRelation = "item")
@Schema(description = "상위 스텝의 세부 정보를 포함한 아이템 객체")
public class ItemDto extends AbstractItemDto {

    @Schema(description = "상위 스탭 객체")
    private SimpleStepDto step;

//    @Schema(description = "이전 스텝의 부모 아이템 객체")
//    private SimpleItemDto parentItem;

    @Builder
    public ItemDto(Integer id, String content, String memo, Integer ownerId, Date createdAt, Date updatedAt, SimpleStepDto step) {
        this.id = id;
        this.content = content;
        this.memo = memo;
        this.ownerId = ownerId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.step = step;
//        this.parentItem = parentItem;
    }

    public static ItemDto toDto(Item entity) {
        return ItemDto.builder()
            .id(entity.getId())
            .content(entity.getContent())
            .memo(entity.getMemo())
            .ownerId(entity.getOwnerId())
            .step(SimpleStepDto.toDto(entity.getStep()))
//            .parentItem(SimpleItemDto.toDto(entity.getParentItem()))
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }

}
