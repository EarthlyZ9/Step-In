package com.earthlyz9.stepin.dto.item;

import com.earthlyz9.stepin.dto.step.StepProjectIdDto;
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
    private StepProjectIdDto step;

    @Builder
    public ItemDto(Integer id, String content, String memo, Integer ownerId, Date createdAt, Date updatedAt, StepProjectIdDto step) {
        this.id = id;
        this.content = content;
        this.memo = memo;
        this.ownerId = ownerId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.step = step;
    }

    public static ItemDto toDto(Item entity) {
        return ItemDto.builder()
            .id(entity.getId())
            .content(entity.getContent())
            .memo(entity.getMemo())
            .ownerId(entity.getOwnerId())
            .step(StepProjectIdDto.toDto(entity.getStep()))
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }

}
