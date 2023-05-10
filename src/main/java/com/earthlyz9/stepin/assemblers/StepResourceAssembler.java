package com.earthlyz9.stepin.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.earthlyz9.stepin.controllers.AuthController;
import com.earthlyz9.stepin.controllers.StepController;
import com.earthlyz9.stepin.dto.step.StepDto;
import com.earthlyz9.stepin.dto.step.StepProjectDto;
import com.earthlyz9.stepin.dto.step.StepProjectIdDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class StepResourceAssembler implements RepresentationModelAssembler<StepDto, EntityModel<StepDto>> {

    @Override
    public CollectionModel<EntityModel<StepDto>> toCollectionModel(
        Iterable<? extends StepDto> steps) {
        int projectId = steps.iterator().next().getId();
        return RepresentationModelAssembler.super.toCollectionModel(steps).add(
            linkTo(methodOn(StepController.class).getAllSteps(projectId)).withSelfRel()
        );
    }

    @Override
    public EntityModel<StepDto> toModel(StepDto step) {
        int stepId = step.getId();
        int projectId;

        if (step.getClass() == StepProjectIdDto.class) projectId = ((StepProjectIdDto) step).getProjectId();
        else projectId = ((StepProjectDto) step).getProject().getId();

        EntityModel<StepDto> entityModel = EntityModel.of(step,
            linkTo(methodOn(StepController.class).getStepById(stepId)).withSelfRel());
        entityModel.add(
            linkTo(methodOn(StepController.class).getAllSteps(projectId)).withRel("collection")
        );
        entityModel.add(
            linkTo(methodOn(AuthController.class).getCurrentUser()).withRel("user")
        );

        return entityModel;
    }
}
