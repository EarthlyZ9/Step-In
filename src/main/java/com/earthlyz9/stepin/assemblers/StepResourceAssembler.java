package com.earthlyz9.stepin.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.earthlyz9.stepin.controllers.AuthController;
import com.earthlyz9.stepin.controllers.StepController;
import com.earthlyz9.stepin.dto.step.AbstractStepDto;
import com.earthlyz9.stepin.dto.step.StepDto;
import com.earthlyz9.stepin.dto.step.SimpleStepDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class StepResourceAssembler implements RepresentationModelAssembler<AbstractStepDto, EntityModel<AbstractStepDto>> {

    @Override
    public CollectionModel<EntityModel<AbstractStepDto>> toCollectionModel(
        Iterable<? extends AbstractStepDto> steps) {
        int projectId = steps.iterator().next().getId();
        return RepresentationModelAssembler.super.toCollectionModel(steps).add(
            linkTo(methodOn(StepController.class).getAllSteps(projectId)).withSelfRel()
        );
    }

    @Override
    public EntityModel<AbstractStepDto> toModel(AbstractStepDto step) {
        int stepId = step.getId();
        int projectId;

        if (step.getClass() == SimpleStepDto.class) projectId = ((SimpleStepDto) step).getProjectId();
        else projectId = ((StepDto) step).getProject().getId();

        EntityModel<AbstractStepDto> entityModel = EntityModel.of(step,
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
