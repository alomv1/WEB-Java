package com.weblabs.webjava.services;

import com.weblabs.webjava.domain.Product;

import java.util.List;
import java.util.UUID;

public interface ProductService {

    Product createProduct(Product product);

    Product getProductById(UUID id);

    List<Product> getAllProducts();

    Product updateProduct(UUID id, Product product);

    void deleteProduct(UUID id);
}