package com.earthlyz9.stepin.services;

import com.earthlyz9.stepin.entities.Category;
import com.earthlyz9.stepin.entities.CategoryPatchRequest;
import com.earthlyz9.stepin.entities.Project;
import com.earthlyz9.stepin.exceptions.NotFoundException;
import com.earthlyz9.stepin.repositories.CategoryRepository;
import java.util.List;
import java.util.Optional;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CategoryServiceImpl implements CategoryService{
    private final CategoryRepository categoryRepository;
    private final ProjectServiceImpl projectServiceImpl;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, ProjectServiceImpl projectServiceImpl) {
        this.categoryRepository = categoryRepository;
        this.projectServiceImpl = projectServiceImpl;
    }

    @Override
    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getCategoryById(Integer categoryId) {
        Optional<Category> category = categoryRepository.findById(categoryId);
        if (category.isEmpty()) throw new NotFoundException("category with the provided id does not exist");
        return category.get();
    }

    @Override
    @Transactional
    public Category createCategory(Category newCategory, Integer projectId) throws NotFoundException {
        int categoryCount = categoryRepository.findAll().size();

        Project currentProject = projectServiceImpl.getProjectById(projectId);
        newCategory.setId(0);
        newCategory.setProjectId(projectId);
        newCategory.setNumber(categoryCount + 1);
        Category category = categoryRepository.save(newCategory);
        category.setProject(currentProject);
        return category;
    }

    @Override
    @Transactional
    public Category partialUpdateCategory(Integer categoryId, CategoryPatchRequest newCategory) throws NotFoundException {
        Category object = getCategoryById(categoryId);
        object.setName(newCategory.getName());
        return categoryRepository.save(object);
    }

    @Override
    @Transactional
    public void deleteCategoryById(Integer categoryId) throws NotFoundException{
        Category instance = getCategoryById(categoryId);
        categoryRepository.deleteById(categoryId);
    }
}
