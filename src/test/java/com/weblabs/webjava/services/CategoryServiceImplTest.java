package com.weblabs.webjava.services;

import com.weblabs.webjava.domain.Category;
import com.weblabs.webjava.repository.CategoryRepository;
import com.weblabs.webjava.services.impl.CategoryServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryRepository repository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    void createCategory_ShouldSave() {
        Category category = new Category();
        when(repository.save(any(Category.class))).thenReturn(category);

        Category result = categoryService.createCategory(category);

        assertNotNull(result);
        verify(repository).save(category);
    }

    @Test
    void getCategoryById_ShouldReturnCategory() {
        UUID id = UUID.randomUUID();
        Category category = new Category();
        when(repository.findById(id)).thenReturn(Optional.of(category));

        Category result = categoryService.getCategoryById(id);

        assertNotNull(result);
    }

    @Test
    void getAllCategories_ShouldReturnList() {
        when(repository.findAll()).thenReturn(Collections.emptyList());

        List<Category> result = categoryService.getAllCategories();

        assertNotNull(result);
        verify(repository).findAll();
    }

    @Test
    void updateCategory_ShouldUpdate() {
        UUID id = UUID.randomUUID();
        Category category = new Category();


        when(repository.existsById(id)).thenReturn(true);
        when(repository.save(any(Category.class))).thenReturn(category);

        Category result = categoryService.updateCategory(id, category);

        assertNotNull(result);
        assertEquals(id, category.getId());
    }

    @Test
    void deleteCategory_ShouldDelete() {
        UUID id = UUID.randomUUID();

        when(repository.existsById(id)).thenReturn(true);

        categoryService.deleteCategory(id);

        verify(repository).deleteById(id);
    }
}