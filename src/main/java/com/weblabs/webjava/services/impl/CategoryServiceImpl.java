package com.weblabs.webjava.services.impl;

import com.weblabs.webjava.domain.Category;
import com.weblabs.webjava.services.CategoryService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final Map<UUID, Category> categoryRepo = new HashMap<>();

    @Override
    public Category createCategory(Category category) {
        UUID id = UUID.randomUUID();
        category.setId(id);
        categoryRepo.put(id, category);
        return category;
    }

    @Override
    public Category getCategoryById(UUID id) {
        Category category = categoryRepo.get(id);
        if (category == null) {
            throw new NoSuchElementException("Category not found with id: " + id);
        }
        return category;
    }

    @Override
    public List<Category> getAllCategories() {
        return new ArrayList<>(categoryRepo.values());
    }

    @Override
    public Category updateCategory(UUID id, Category category) {
        if (!categoryRepo.containsKey(id)) {
            throw new NoSuchElementException("Category not found with id: " + id);
        }
        category.setId(id);
        categoryRepo.put(id, category);
        return category;
    }

    @Override
    public void deleteCategory(UUID id) {
        if (!categoryRepo.containsKey(id)) {
            throw new NoSuchElementException("Category not found with id: " + id);
        }
        categoryRepo.remove(id);
    }
}