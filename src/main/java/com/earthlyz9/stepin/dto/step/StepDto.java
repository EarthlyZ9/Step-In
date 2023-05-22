package com.earthlyz9.stepin.dto.step;

import com.earthlyz9.stepin.dto.item.ItemDto;
import com.earthlyz9.stepin.dto.project.AbstractProjectDto;
import com.earthlyz9.stepin.dto.project.SimpleProjectDto;
import com.earthlyz9.stepin.entities.Item;
import com.earthlyz9.stepin.entities.Step;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.server.core.Relation;

@Getter
@Setter
@Relation(collectionRelation = "steps", itemRelation = "step")
@Schema(description = "프로젝트 세부 정보가 포함된 스텝 객체")
public class StepDto extends AbstractStepDto {

    @Schema(description = "스텝이 포함된 프로젝트 객체")
    private AbstractProjectDto project;

    @Schema(description = "스텝 하위의 items")
    private List<ItemDto> items;

    @Builder
    public StepDto(Integer id, String name, Integer number, Integer ownerId, LocalDateTime createdAt, LocalDateTime updatedAt, AbstractProjectDto project) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.ownerId = ownerId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.project = project;
    }

    public static StepDto toDto(Step entity) {
        StepDto stepDto =  StepDto.builder()
            .id(entity.getId())
            .name(entity.getName())
            .number(entity.getNumber())
            .ownerId(entity.getOwnerId())
            .project(SimpleProjectDto.toDto(entity.getProject()))
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();

        List<Item> itemList = entity.getItems();
        if (itemList == null) stepDto.setItems(Collections.emptyList());
        else {
            List<ItemDto> itemDtos = itemList.stream()
                .map(ItemDto::toDto).toList();
            stepDto.setItems(itemDtos);
        }

        return stepDto;
    }

}
