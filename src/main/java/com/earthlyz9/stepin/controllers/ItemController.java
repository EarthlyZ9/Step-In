package com.earthlyz9.stepin.controllers;

import com.earthlyz9.stepin.entities.Item;
import com.earthlyz9.stepin.entities.ItemPatchRequest;
import com.earthlyz9.stepin.exceptions.NotFoundException;
import com.earthlyz9.stepin.services.ItemServiceImpl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/items")
public class ItemController {
    private final ItemServiceImpl itemServiceImpl;

    @Autowired
    public ItemController(ItemServiceImpl itemServiceImpl) {
        this.itemServiceImpl = itemServiceImpl;
    }

    @GetMapping("")
    public List<Item> getAllItems() {
        return itemServiceImpl.getItems();
    }

    @GetMapping("/{itemId}")
    public Item getItemById(@PathVariable int itemId) {
        return itemServiceImpl.getItemById(itemId);
    }

    @PatchMapping("/{itemId}")
    public Item updateItemById(@PathVariable int itemId, @RequestBody ItemPatchRequest data) throws NotFoundException {
        Item updatedItem = itemServiceImpl.partialUpdateItem(itemId, data);
        return updatedItem;
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> deleteItemById(@PathVariable int itemId) throws NotFoundException {
        itemServiceImpl.deleteItemById(itemId);
        return ResponseEntity.noContent().build();
    }
}
