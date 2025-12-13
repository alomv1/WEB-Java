package com.weblabs.webjava.services.impl;

import com.weblabs.webjava.domain.Category;
import com.weblabs.webjava.exception.PersistenceException;
import com.weblabs.webjava.repository.CategoryRepository;
import com.weblabs.webjava.services.CategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository repository;

    public CategoryServiceImpl(CategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public Category createCategory(Category category) {
        try {
            return repository.save(category);
        } catch (Exception e) {
            throw new PersistenceException("Failed to create category", e);
        }
    }

    @Override
    public Category getCategoryById(UUID id) {
        try {
            return repository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Category not found with id: " + id));
        } catch (NoSuchElementException e) {
            throw e;
        } catch (Exception e) {
            throw new PersistenceException("Failed to fetch category with id: " + id, e);
        }
    }

    @Override
    public List<Category> getAllCategories() {
        try {
            return repository.findAll();
        } catch (Exception e) {
            throw new PersistenceException("Failed to fetch all categories", e);
        }
    }

    @Override
    public Category updateCategory(UUID id, Category category) {
        try {
            if (!repository.existsById(id)) {
                throw new NoSuchElementException("Category not found with id: " + id);
            }
            category.setId(id);
            return repository.save(category);
        } catch (NoSuchElementException e) {
            throw e;
        } catch (Exception e) {
            throw new PersistenceException("Failed to update category with id: " + id, e);
        }
    }

    @Override
    public void deleteCategory(UUID id) {
        try {
            if (repository.existsById(id)) {
                repository.deleteById(id);
            }
        } catch (Exception e) {
            throw new PersistenceException("Failed to delete category with id: " + id, e);
        }
    }
}