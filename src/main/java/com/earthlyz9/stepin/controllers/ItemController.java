package com.earthlyz9.stepin.controllers;

import com.earthlyz9.stepin.JsonViews;
import com.earthlyz9.stepin.entities.Item;
import com.earthlyz9.stepin.dto.ItemPatchRequest;
import com.earthlyz9.stepin.exceptions.NotFoundException;
import com.earthlyz9.stepin.services.ItemServiceImpl;
import com.fasterxml.jackson.annotation.JsonView;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.net.URI;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public ItemController(ItemServiceImpl itemServiceImpl) {
        this.itemServiceImpl = itemServiceImpl;
    }

    @PostMapping("/steps/{stepId}/items")
    @JsonView(JsonViews.Retrieve.class)
    @Operation(summary = "해당 스텝 id 하위에 새로운 아이템을 추가합니다", responses = {
        @ApiResponse(description = "created", responseCode = "201", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<Item> createItem(@PathVariable int stepId, @RequestBody Item item) throws NotFoundException {
        Item newItem = itemServiceImpl.createItem(item, stepId);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{itemId}")
            .buildAndExpand(newItem.getId()).toUri();
        return ResponseEntity.created(location).body(newItem);
    }

    @GetMapping("/steps/{stepId}/items")
    @JsonView(JsonViews.List.class)
    @Operation(summary = "해당 스텝 id 하위의 모든 아이템을 가져옵니다", responses = {
        @ApiResponse(description = "ok", responseCode = "200", content = @Content(mediaType = "application/json"))
    })
    public List<Item> getItemsByStepId(@PathVariable int stepId) {
        return itemServiceImpl.getItemsByStepId(stepId);
    }

    @GetMapping("/items/{itemId}")
    @JsonView(JsonViews.Retrieve.class)
    @Operation(summary = "해당 id 의 아이템을 가져옵니다", responses = {
        @ApiResponse(description = "ok", responseCode = "200", content = @Content(mediaType = "application/json"))
    })
    public Item getItemById(@PathVariable int itemId) {
        return itemServiceImpl.getItemById(itemId);
    }

    @PatchMapping("/items/{itemId}")
    @JsonView(JsonViews.Retrieve.class)
    @Operation(summary = "해당 id를 가진 아이템의 내용을 수정합니다", responses = {
        @ApiResponse(description = "ok", responseCode = "200", content = @Content(mediaType = "application/json"))
    })
    public Item updateItemById(@PathVariable int itemId, @RequestBody ItemPatchRequest data) throws NotFoundException {
        Item updatedItem = itemServiceImpl.partialUpdateItem(itemId, data);
        return updatedItem;
    }

    @DeleteMapping("/items/{itemId}")
    @Operation(summary = "해당 id를 가진 아이템을 삭제합니다", responses = {
        @ApiResponse(description = "ok", responseCode = "200", content = @Content)
    })
    public ResponseEntity<Void> deleteItemById(@PathVariable int itemId) throws NotFoundException {
        itemServiceImpl.deleteItemById(itemId);
        return ResponseEntity.noContent().build();
    }
}
