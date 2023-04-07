package com.earthlyz9.stepin.controllers;

import com.earthlyz9.stepin.entities.Category;
import com.earthlyz9.stepin.entities.CategoryPatchRequest;
import com.earthlyz9.stepin.entities.Item;
import com.earthlyz9.stepin.exceptions.NotFoundException;
import com.earthlyz9.stepin.services.CategoryServiceImpl;
import com.earthlyz9.stepin.services.ItemServiceImpl;
import java.net.URI;
import java.util.List;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryServiceImpl categoryServiceImpl;
    private final ItemServiceImpl itemServiceImpl;

    @Autowired
    public CategoryController(CategoryServiceImpl categoryServiceImpl, ItemServiceImpl itemServiceImpl) {
        this.categoryServiceImpl = categoryServiceImpl;
        this.itemServiceImpl = itemServiceImpl;
    }

    @GetMapping("")
    public List<Category> getAllCategories() {
        return categoryServiceImpl.getCategories();
    }

    @GetMapping("/{categoryId}")
    public Category getCategoryById(@PathVariable int categoryId) throws NotFoundException {
        return categoryServiceImpl.getCategoryById(categoryId);
    }

    @PatchMapping("/{categoryId}")
    public Category updateCategoryById(@PathVariable int categoryId, @RequestBody
        CategoryPatchRequest data) throws NotFoundException {
        Category updatedCategory = categoryServiceImpl.partialUpdateCategory(categoryId, data);
        return updatedCategory;
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategoryById(@PathVariable int categoryId) throws NotFoundException {
        categoryServiceImpl.deleteCategoryById(categoryId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{categoryId}/items")
    public ResponseEntity<Item> createItem(@PathVariable int categoryId, @RequestBody Item item) throws NotFoundException {
        Item newItem = itemServiceImpl.createItem(item, categoryId);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{itemId}")
            .buildAndExpand(newItem.getId()).toUri();
        return ResponseEntity.created(location).body(newItem);
    }

}
