package com.weblabs.webjava.repository;

import com.weblabs.webjava.entity.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Optional;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProductRepositoryIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private ProductRepository productRepository;

    @Test
    void shouldPerformFullCrudOperations() {
        Product product = new Product();
        product.setName("Original Name");
        product.setPrice(100.0);
        product.setStock(10);

        Product savedProduct = productRepository.save(product);
        Assertions.assertNotNull(savedProduct.getId());

        Product foundProduct = productRepository.findById(savedProduct.getId()).orElseThrow();
        Assertions.assertEquals("Original Name", foundProduct.getName());

        foundProduct.setName("Updated Name");
        foundProduct.setPrice(200.0);
        Product updatedProduct = productRepository.save(foundProduct);

        Assertions.assertEquals("Updated Name", updatedProduct.getName());
        Assertions.assertEquals(200.0, updatedProduct.getPrice());

        productRepository.delete(updatedProduct);

        Optional<Product> deletedProduct = productRepository.findById(savedProduct.getId());
        Assertions.assertTrue(deletedProduct.isEmpty());
    }
}