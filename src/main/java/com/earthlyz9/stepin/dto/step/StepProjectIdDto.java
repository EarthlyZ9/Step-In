package com.earthlyz9.stepin.dto.step;

import com.earthlyz9.stepin.entities.Step;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.server.core.Relation;

@Getter
@Setter
@Relation(collectionRelation = "steps", itemRelation = "step")
@Schema(description = "프로젝트 세부 정보가 제외된 스텝 객체")
public class StepProjectIdDto extends StepDto {
    @Schema(description = "스텝이 포함된 프로젝트의 id")
    @JsonProperty(access = Access.READ_ONLY)
    private Integer projectId;

    @Builder
    public StepProjectIdDto(Integer id, String name, Integer number,Integer ownerId, Date createdAt, Date updatedAt, Integer projectId) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.projectId = projectId;
        this.ownerId = ownerId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static StepProjectIdDto toDto(Step entity) {
        return StepProjectIdDto.builder()
            .id(entity.getId())
            .name(entity.getName())
            .number(entity.getNumber())
            .projectId(entity.getProjectId())
            .ownerId(entity.getOwnerId())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }

    public static Step toEntity(StepProjectIdDto dto) {
        return Step.builder()
            .id(dto.getId())
            .name(dto.getName())
            .number(dto.getNumber())
            .projectId(dto.getProjectId())
            .ownerId(dto.getOwnerId())
            .build();
    }

}
