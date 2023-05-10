package com.earthlyz9.stepin.dto.step;

import com.earthlyz9.stepin.dto.project.ProjectDto;
import com.earthlyz9.stepin.dto.project.ProjectOwnerIdDto;
import com.earthlyz9.stepin.entities.Step;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.server.core.Relation;

@Getter
@Setter
@Relation(collectionRelation = "steps", itemRelation = "step")
@Schema(description = "프로젝트 세부 정보가 포함된 스텝 객체")
public class StepProjectDto extends StepDto {

    @Schema(description = "스텝이 포함된 프로젝트 객체")
    private ProjectDto project;

    @Builder
    public StepProjectDto(Integer id, String name, Integer number, Integer ownerId, Date createdAt, Date updatedAt, ProjectDto project) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.ownerId = ownerId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.project = project;
    }

    public static StepProjectDto toDto(Step entity) {
        return StepProjectDto.builder()
            .id(entity.getId())
            .name(entity.getName())
            .number(entity.getNumber())
            .ownerId(entity.getOwnerId())
            .project(ProjectOwnerIdDto.toDto(entity.getProject()))
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();
    }

}
