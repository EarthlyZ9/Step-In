package com.earthlyz9.stepin.dto.item;

import com.earthlyz9.stepin.entities.Item;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.server.core.Relation;

@Getter
@Setter
@Relation(collectionRelation = "items", itemRelation = "item")
@Schema(description = "상위 스텝 세부정보가 제외된 아이템 객")
public class SimpleItemDto extends AbstractItemDto {

    @Schema(description = "상위 스텝의 아이디")
    private Integer stepId;

    @Builder
    public SimpleItemDto(Integer id, String content, String memo, Integer ownerId, LocalDateTime createdAt, LocalDateTime updatedAt, Integer stepId, Integer parentItemId) {
        this.id = id;
        this.content = content;
        this.memo = memo;
        this.ownerId = ownerId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.stepId = stepId;
        this.parentItemId = parentItemId;
    }

    public static SimpleItemDto toDto(Item entity) {
        return SimpleItemDto.builder()
            .id(entity.getId())
            .content(entity.getContent())
            .memo(entity.getMemo())
            .ownerId(entity.getOwnerId())
            .stepId(entity.getStepId())
            .parentItemId(entity.getParentItemId())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }
}
