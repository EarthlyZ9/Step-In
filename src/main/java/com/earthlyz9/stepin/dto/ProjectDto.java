package com.earthlyz9.stepin.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

@Setter
@Getter
public abstract class ProjectDto extends RepresentationModel<ProjectDto> {
    protected Integer id;

    @Schema(description = "프로젝트 이름")
    protected String name;
    protected Date createdAt;
    protected Date updatedAt;
}
