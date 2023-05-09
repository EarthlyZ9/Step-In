package com.earthlyz9.stepin.dto;

import com.earthlyz9.stepin.entities.Project;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.server.core.Relation;

@Setter
@Getter
@Relation(collectionRelation = "projects", itemRelation = "project")
@Schema(description = "유저 세부 정보가 제외된 프로젝트 객체")
public class ProjectOwnerIdDto extends ProjectDto {
    @Schema(description = "프로젝트를 생성한 유저의 id")
    @JsonProperty(access = Access.READ_ONLY)
    private Integer ownerId;

    @Builder
    public ProjectOwnerIdDto(Integer id, String name, Date createdAt, Date updatedAt, Integer ownerId) {
        this.id = id;
        this.name = name;
        this.ownerId = ownerId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static ProjectOwnerIdDto toDto(Project entity) {
        return ProjectOwnerIdDto.builder()
            .id(entity.getId())
            .name(entity.getName())
            .ownerId(entity.getOwnerId())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }
}
