package com.earthlyz9.stepin.dto.project;

import com.earthlyz9.stepin.entities.Project;
import com.earthlyz9.stepin.entities.User;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.server.core.Relation;

@Setter
@Getter
@Relation(collectionRelation = "projects", itemRelation = "project")
@Schema(description = "유저 세부 정보가 포함된 프로젝트 객체")
public class ProjectDto extends AbstractProjectDto {
    @Schema(description = "프로젝트를 생성한 유저 객체")
    private User owner;

    @Builder
    public ProjectDto(Integer id, String name, Date createdAt, Date updatedAt, User owner) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static ProjectDto toDto(Project entity) {
        return ProjectDto.builder()
            .id(entity.getId())
            .name(entity.getName())
            .owner(entity.getOwner())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }
}
