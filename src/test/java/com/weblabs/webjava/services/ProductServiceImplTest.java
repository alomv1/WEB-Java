package com.weblabs.webjava.services;

import com.weblabs.webjava.domain.Product;
import com.weblabs.webjava.repository.ProductRepository;
import com.weblabs.webjava.services.impl.ProductServiceImpl;
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
class ProductServiceImplTest {

    @Mock
    private ProductRepository repository;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void createProduct_ShouldReturnProduct() {
        Product product = new Product();
        product.setName("Test Product");
        product.setPrice(100.0);

        when(repository.save(any(Product.class))).thenReturn(product);

        Product created = productService.createProduct(product);

        assertNotNull(created);
        assertEquals("Test Product", created.getName());
        verify(repository).save(product);
    }

    @Test
    void getProductById_ShouldReturnProduct() {
        UUID id = UUID.randomUUID();
        Product product = new Product();
        product.setId(id);

        when(repository.findById(id)).thenReturn(Optional.of(product));

        Product found = productService.getProductById(id);

        assertNotNull(found);
        assertEquals(id, found.getId());
    }

    @Test
    void getProductById_ShouldThrow_WhenNotFound() {
        UUID id = UUID.randomUUID();

        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> productService.getProductById(id));
    }

    @Test
    void updateProduct_ShouldUpdateFields() {
        UUID id = UUID.randomUUID();
        Product updateInfo = new Product();
        updateInfo.setName("New Name");
        updateInfo.setPrice(200.0);

        when(repository.existsById(id)).thenReturn(true);
        when(repository.save(any(Product.class))).thenReturn(updateInfo);

        Product updated = productService.updateProduct(id, updateInfo);

        assertNotNull(updated);
        assertEquals(id, updateInfo.getId());
        assertEquals("New Name", updated.getName());
    }

    @Test
    void deleteProduct_ShouldCallDelete() {
        UUID id = UUID.randomUUID();

        when(repository.existsById(id)).thenReturn(true);

        productService.deleteProduct(id);

        verify(repository, times(1)).deleteById(id);
    }
}