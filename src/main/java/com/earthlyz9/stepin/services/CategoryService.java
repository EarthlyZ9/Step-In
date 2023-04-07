package com.earthlyz9.stepin.services;

import com.earthlyz9.stepin.entities.Category;
import com.earthlyz9.stepin.entities.CategoryPatchRequest;
import java.util.List;

public interface CategoryService {
    List<Category> getCategories();

    Category getCategoryById(Integer categoryId);

    Category createCategory(Category newCategory, Integer projectId);

    Category partialUpdateCategory(Integer categoryId, CategoryPatchRequest newCategory);

    void deleteCategoryById(Integer categoryId);

}
