package com.earthlyz9.stepin.controllers;

import com.earthlyz9.stepin.assemblers.ItemResourceAssembler;
import com.earthlyz9.stepin.dto.item.AbstractItemDto;
import com.earthlyz9.stepin.dto.item.ItemDto;
import com.earthlyz9.stepin.dto.item.SimpleItemDto;
import com.earthlyz9.stepin.entities.Item;
import com.earthlyz9.stepin.dto.item.ItemPatchRequest;
import com.earthlyz9.stepin.exceptions.ExceptionResponse;
import com.earthlyz9.stepin.exceptions.NotFoundException;
import com.earthlyz9.stepin.exceptions.ValidationExceptionReponse;
import com.earthlyz9.stepin.services.ItemServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@Tag(name = "Item", description = "카테고리 하위의 아이템")
public class ItemController {
    private final ItemServiceImpl itemServiceImpl;
    private final ItemResourceAssembler assembler;

    @Autowired
    public ItemController(ItemServiceImpl itemServiceImpl, ItemResourceAssembler assembler) {
        this.itemServiceImpl = itemServiceImpl;
        this.assembler = assembler;
    }

    @PostMapping("/steps/{stepId}/items")
    @Operation(summary = "해당 스텝 id 하위에 새로운 아이템을 추가합니다", responses = {
        @ApiResponse(description = "created", responseCode = "201", content = @Content(mediaType = "application/hal+json", schema = @Schema(implementation = ItemDto.class))),
        @ApiResponse(description = "validation error", responseCode = "400", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ValidationExceptionReponse.class)))
    })
    public ResponseEntity<EntityModel<AbstractItemDto>> createItem(@PathVariable int stepId, @RequestBody Item item) throws NotFoundException {
        Item newItem = itemServiceImpl.createItem(item, stepId);
        ItemDto dto = ItemDto.toDto(newItem);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{itemId}")
            .buildAndExpand(newItem.getId()).toUri();
        return ResponseEntity.created(location).body(assembler.toModel(dto));
    }

    @GetMapping("/steps/{stepId}/items")
    @Operation(summary = "해당 스텝 id 하위의 모든 아이템을 가져옵니다", responses = {
        @ApiResponse(description = "ok", responseCode = "200", content = @Content(mediaType = "application/hal+json", array = @ArraySchema(
            schema = @Schema(implementation = SimpleItemDto.class)
        ))),
        @ApiResponse(description = "not found", responseCode = "404", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
    })
    public CollectionModel<EntityModel<AbstractItemDto>> getItemsByStepId(@PathVariable int stepId) throws NotFoundException {
        List<Item> items = itemServiceImpl.getItemsByStepId(stepId);
        List<SimpleItemDto> collection = items.stream()
            .map(SimpleItemDto::toDto).toList();
        return assembler.toCollectionModel(collection);
    }

    @GetMapping("/items/{itemId}")
    @Operation(summary = "해당 id 의 아이템을 가져옵니다", responses = {
        @ApiResponse(description = "ok", responseCode = "200", content = @Content(mediaType = "application/json")),
        @ApiResponse(description = "not found", responseCode = "404", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
    })
    public EntityModel<AbstractItemDto> getItemById(@PathVariable int itemId) {
        Item item = itemServiceImpl.getItemById(itemId);

        return assembler.toModel(ItemDto.toDto(item));
    }

    @PatchMapping("/items/{itemId}")
    @Operation(summary = "해당 id를 가진 아이템의 내용을 수정합니다", responses = {
        @ApiResponse(description = "ok", responseCode = "200", content = @Content(mediaType = "application/json")),
        @ApiResponse(description = "validation error", responseCode = "400", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ValidationExceptionReponse.class))),
        @ApiResponse(description = "not found", responseCode = "404", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
    })
    public EntityModel<AbstractItemDto> updateItemById(@PathVariable int itemId, @RequestBody ItemPatchRequest data) throws NotFoundException {
        Item updatedItem = itemServiceImpl.partialUpdateItem(itemId, data);

        return assembler.toModel(ItemDto.toDto(updatedItem));
    }

    @DeleteMapping("/items/{itemId}")
    @Operation(summary = "해당 id를 가진 아이템을 삭제합니다", responses = {
        @ApiResponse(description = "ok", responseCode = "204"),
        @ApiResponse(description = "not found", responseCode = "404", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class)))
    })
    public ResponseEntity<Void> deleteItemById(@PathVariable int itemId) throws NotFoundException {
        itemServiceImpl.deleteItemById(itemId);
        return ResponseEntity.noContent().build();
    }
}
