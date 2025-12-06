package com.weblabs.webjava.services;

import com.weblabs.webjava.domain.Category;
import com.weblabs.webjava.services.impl.CategoryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CategoryServiceImplTest {

    private CategoryServiceImpl categoryService;

    @BeforeEach
    void setUp() {
        categoryService = new CategoryServiceImpl();
    }

    @Test
    void createCategory_ShouldAssignId() {
        Category category = new Category();
        category.setName("Electronics");

        Category saved = categoryService.createCategory(category);

        assertNotNull(saved.getId());
        assertEquals("Electronics", saved.getName());
    }

    @Test
    void getCategoryById_ShouldThrow_IfNotFound() {
        assertThrows(NoSuchElementException.class, () -> categoryService.getCategoryById(UUID.randomUUID()));
    }

    @Test
    void updateCategory_ShouldUpdateData() {
        Category category = new Category();
        category.setName("Old");
        Category saved = categoryService.createCategory(category);
        UUID id = saved.getId();

        Category updateData = new Category();
        updateData.setName("New");

        Category updated = categoryService.updateCategory(id, updateData);
        assertEquals("New", updated.getName());
    }

    @Test
    void deleteCategory_ShouldRemove() {
        Category category = new Category();
        Category saved = categoryService.createCategory(category);

        categoryService.deleteCategory(saved.getId());

        assertThrows(NoSuchElementException.class, () -> categoryService.getCategoryById(saved.getId()));
    }
}