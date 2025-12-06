package com.weblabs.webjava.services;

import com.weblabs.webjava.domain.Category;

import java.util.List;
import java.util.UUID;

public interface CategoryService {

    Category createCategory(Category category);

    Category getCategoryById(UUID id);

    List<Category> getAllCategories();

    Category updateCategory(UUID id, Category category);

    void deleteCategory(UUID id);
}