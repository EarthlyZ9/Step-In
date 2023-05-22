package com.earthlyz9.stepin.dto.project;

import com.earthlyz9.stepin.entities.Project;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.server.core.Relation;

@Setter
@Getter
@Relation(collectionRelation = "projects", itemRelation = "project")
@Schema(description = "유저 세부 정보가 제외된 프로젝트 객체")
public class SimpleProjectDto extends AbstractProjectDto {
    @Schema(description = "프로젝트를 생성한 유저의 id")
    @JsonProperty(access = Access.READ_ONLY)
    private Integer ownerId;

    @Builder
    public SimpleProjectDto(Integer id, String name, LocalDateTime createdAt, LocalDateTime updatedAt, Integer ownerId) {
        this.id = id;
        this.name = name;
        this.ownerId = ownerId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static SimpleProjectDto toDto(Project entity) {
        return SimpleProjectDto.builder()
            .id(entity.getId())
            .name(entity.getName())
            .ownerId(entity.getOwnerId())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }
}
