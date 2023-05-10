package com.earthlyz9.stepin.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.earthlyz9.stepin.controllers.AuthController;
import com.earthlyz9.stepin.controllers.ItemController;
import com.earthlyz9.stepin.dto.item.AbstractItemDto;
import com.earthlyz9.stepin.dto.item.ItemDto;
import com.earthlyz9.stepin.dto.item.SimpleItemDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class ItemResourceAssembler implements RepresentationModelAssembler<AbstractItemDto, EntityModel<AbstractItemDto>> {

    @Override
    public CollectionModel<EntityModel<AbstractItemDto>> toCollectionModel(
        Iterable<? extends AbstractItemDto> items) {
        int stepId;
        if (items.iterator().hasNext()) stepId = items.iterator().next().getId();
        else return RepresentationModelAssembler.super.toCollectionModel(items);
        return RepresentationModelAssembler.super.toCollectionModel(items).add(
            linkTo(methodOn(ItemController.class).getItemsByStepId(stepId)).withSelfRel()
        );
    }

    @Override
    public EntityModel<AbstractItemDto> toModel(AbstractItemDto item) {
        int itemId = item.getId();
        int stepId;

        if (item.getClass() == SimpleItemDto.class) stepId = ((SimpleItemDto) item).getStepId();
        else stepId = ((ItemDto) item).getStep().getId();

        EntityModel<AbstractItemDto> entityModel = EntityModel.of(item,
            linkTo(methodOn(ItemController.class).getItemById(itemId)).withSelfRel());
        entityModel.add(
            linkTo(methodOn(ItemController.class).getItemsByStepId(stepId)).withRel("collection")
        );
        entityModel.add(
            linkTo(methodOn(AuthController.class).getCurrentUser()).withRel("user")
        );

        return entityModel;
    }
}
