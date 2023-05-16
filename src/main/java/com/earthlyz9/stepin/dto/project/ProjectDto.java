package com.earthlyz9.stepin.dto.project;

import com.earthlyz9.stepin.dto.step.StepDto;
import com.earthlyz9.stepin.entities.Project;
import com.earthlyz9.stepin.entities.Step;
import com.earthlyz9.stepin.entities.User;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Collections;
import java.util.Date;
import java.util.List;
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

    @Schema(description = "프로젝트 하위의 steps")
    private List<StepDto> steps;

    @Builder
    public ProjectDto(Integer id, String name, Date createdAt, Date updatedAt, User owner) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static ProjectDto toDto(Project entity) {
        ProjectDto projectDto = ProjectDto.builder()
            .id(entity.getId())
            .name(entity.getName())
            .owner(entity.getOwner())
            .createdAt(entity.getCreatedAt())
            .updatedAt(entity.getUpdatedAt())
            .build();

        List<Step> stepList = entity.getSteps();
        if (stepList.isEmpty()) projectDto.setSteps(Collections.emptyList());
        else {
            List<StepDto> stepDtos = stepList.stream()
                .map(StepDto::toDto).toList();
            projectDto.setSteps(stepDtos);
        }

        return projectDto;
    }
}
