package com.weblabs.webjava.services.impl;

import com.weblabs.webjava.domain.Product;
import com.weblabs.webjava.services.ProductService;

import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductServiceImpl implements ProductService {

    private final Map<UUID, Product> productRepo = new HashMap<>();

    @Override
    public Product createProduct(Product product) {
        UUID id = UUID.randomUUID();
        product.setId(id);
        productRepo.put(id, product);
        return product;
    }

    @Override
    public Product getProductById(UUID id) {
        Product product = productRepo.get(id);
        if (product == null) {
            throw new NoSuchElementException("Product not found with id: " + id);
        }
        return product;
    }

    @Override
    public List<Product> getAllProducts() {
        return new ArrayList<>(productRepo.values());
    }

    @Override
    public Product updateProduct(UUID id, Product product) {
        if (!productRepo.containsKey(id)) {
            throw new NoSuchElementException("Product not found with id: " + id);
        }
        product.setId(id);
        productRepo.put(id, product);
        return product;
    }

    @Override
    public void deleteProduct(UUID id) {
        if (!productRepo.containsKey(id)) {
            throw new NoSuchElementException("Product not found with id: " + id);
        }
        productRepo.remove(id);
    }
}