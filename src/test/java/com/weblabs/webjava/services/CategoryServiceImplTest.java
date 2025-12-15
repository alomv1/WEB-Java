package com.weblabs.webjava.services;

import com.weblabs.webjava.entity.Category;
import com.weblabs.webjava.repository.CategoryRepository;
import com.weblabs.webjava.services.impl.CategoryServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
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
    private CategoryServiceImpl service;

    @Test
    void createCategory_ShouldSave() {
        Category category = new Category();
        when(repository.save(any())).thenReturn(category);

        Category result = service.createCategory(category);
        assertNotNull(result);
    }

    @Test
    void getCategoryById_ShouldReturn_WhenExists() {
        UUID id = UUID.randomUUID();
        Category category = new Category();
        when(repository.findById(id)).thenReturn(Optional.of(category));

        Category result = service.getCategoryById(id);
        assertEquals(category, result);
    }

    @Test
    void getCategoryById_ShouldThrow_WhenMissing() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> service.getCategoryById(id));
    }

    @Test
    void getAllCategories_ShouldReturnList() {
        service.getAllCategories();
        verify(repository).findAll();
    }

    @Test
    void updateCategory_ShouldUpdate_WhenExists() {
        UUID id = UUID.randomUUID();
        Category category = new Category();
        when(repository.existsById(id)).thenReturn(true);
        when(repository.save(any())).thenReturn(category);

        service.updateCategory(id, category);
        assertEquals(id, category.getId());
    }

    @Test
    void updateCategory_ShouldThrow_WhenMissing() {
        UUID id = UUID.randomUUID();
        when(repository.existsById(id)).thenReturn(false);

        assertThrows(NoSuchElementException.class, () -> service.updateCategory(id, new Category()));
    }

    @Test
    void deleteCategory_ShouldDelete_WhenExists() {
        UUID id = UUID.randomUUID();
        when(repository.existsById(id)).thenReturn(true);

        service.deleteCategory(id);
        verify(repository).deleteById(id);
    }

    @Test
    void deleteCategory_ShouldIgnore_WhenMissing() {
        UUID id = UUID.randomUUID();
        when(repository.existsById(id)).thenReturn(false);

        service.deleteCategory(id);
        verify(repository, never()).deleteById(any());
    }
}