package com.weblabs.webjava.services;

import com.weblabs.webjava.domain.Product;
import com.weblabs.webjava.services.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ProductServiceImplTest {

    private ProductServiceImpl productService;

    @BeforeEach
    void setUp() {
        productService = new ProductServiceImpl();
    }

    @Test
    void createProduct_ShouldReturnProductWithId() {
        Product product = new Product(null, "Test Product", "Desc", 100.0, 10, null);
        Product created = productService.createProduct(product);

        assertNotNull(created.getId());
        assertEquals("Test Product", created.getName());
    }

    @Test
    void getProductById_ShouldReturnProduct() {
        Product product = new Product(null, "Test Product", "Desc", 100.0, 10, null);
        Product created = productService.createProduct(product);

        Product found = productService.getProductById(created.getId());
        assertEquals(created.getId(), found.getId());
    }

    @Test
    void getProductById_ShouldThrowException_WhenNotFound() {
        assertThrows(NoSuchElementException.class, () -> productService.getProductById(UUID.randomUUID()));
    }

    @Test
    void updateProduct_ShouldUpdateFields() {
        Product product = productService.createProduct(new Product(null, "Old Name", "Desc", 100.0, 10, null));
        UUID id = product.getId();

        Product updateInfo = new Product(null, "New Name", "Desc", 200.0, 5, null);
        Product updated = productService.updateProduct(id, updateInfo);

        assertEquals("New Name", updated.getName());
        assertEquals(200.0, updated.getPrice());
    }

    @Test
    void deleteProduct_ShouldRemoveProduct() {
        Product product = productService.createProduct(new Product(null, "To Delete", "Desc", 100.0, 10, null));
        productService.deleteProduct(product.getId());

        assertThrows(NoSuchElementException.class, () -> productService.getProductById(product.getId()));
    }
}