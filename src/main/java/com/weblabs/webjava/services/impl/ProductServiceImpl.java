package com.weblabs.webjava.services.impl;

import com.weblabs.webjava.entity.Product;
import com.weblabs.webjava.exception.PersistenceException;
import com.weblabs.webjava.repository.ProductRepository;
import com.weblabs.webjava.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public Product createProduct(Product product) {
        try {
            return productRepository.save(product);
        } catch (Exception e) {
            throw new PersistenceException("Failed to create product", e);
        }
    }

    @Override
    public Product getProductById(UUID id) {
        try {
            return productRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Product not found with id: " + id));
        } catch (NoSuchElementException e) {
            throw e;
        } catch (Exception e) {
            throw new PersistenceException("Failed to fetch product with id: " + id, e);
        }
    }

    @Override
    public List<Product> getAllProducts() {
        try {
            return productRepository.findAll();
        } catch (Exception e) {
            throw new PersistenceException("Failed to fetch all products", e);
        }
    }

    @Override
    public Product updateProduct(UUID id, Product product) {
        try {
            if (!productRepository.existsById(id)) {
                throw new NoSuchElementException("Product not found with id: " + id);
            }
            product.setId(id);
            return productRepository.save(product);
        } catch (NoSuchElementException e) {
            throw e;
        } catch (Exception e) {
            throw new PersistenceException("Failed to update product with id: " + id, e);
        }
    }

    @Override
    public void deleteProduct(UUID id) {
        try {
            if (productRepository.existsById(id)) {
                productRepository.deleteById(id);
            }
        } catch (Exception e) {
            throw new PersistenceException("Failed to delete product with id: " + id, e);
        }
    }
}