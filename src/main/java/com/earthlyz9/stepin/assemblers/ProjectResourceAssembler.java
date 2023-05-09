package com.earthlyz9.stepin.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.earthlyz9.stepin.controllers.AuthController;
import com.earthlyz9.stepin.controllers.ProjectController;
import com.earthlyz9.stepin.dto.project.ProjectDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class ProjectResourceAssembler implements RepresentationModelAssembler<ProjectDto, EntityModel<ProjectDto>> {
    @Override
    public CollectionModel<EntityModel<ProjectDto>> toCollectionModel(Iterable<? extends ProjectDto> projects) {
        return RepresentationModelAssembler.super.toCollectionModel(projects).add(
            linkTo(methodOn(ProjectController.class).getAllProjects()).withSelfRel()
        );
    }

    @Override
    public EntityModel<ProjectDto> toModel(ProjectDto project) {
        int projectId = project.getId();

        EntityModel<ProjectDto> entityModel = EntityModel.of(project,
            linkTo(methodOn(ProjectController.class).getProjectById(projectId)).withSelfRel());
        entityModel.add(
            linkTo(methodOn(ProjectController.class).getAllProjects()).withRel("collection"));
        entityModel.add(
            linkTo(methodOn(AuthController.class).getCurrentUser()).withRel("user")
        );

        return entityModel;
    }
}
