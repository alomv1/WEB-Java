package com.weblabs.webjava.services;

import com.weblabs.webjava.entity.Product;
import com.weblabs.webjava.repository.ProductRepository;
import com.weblabs.webjava.services.impl.ProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
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
    void createProduct_ShouldSaveAndReturn() {
        Product product = new Product();
        product.setName("Test");
        when(repository.save(any(Product.class))).thenReturn(product);

        Product result = productService.createProduct(product);

        assertNotNull(result);
        verify(repository).save(product);
    }

    @Test
    void getProductById_ShouldReturnProduct_WhenExists() {
        UUID id = UUID.randomUUID();
        Product product = new Product();
        when(repository.findById(id)).thenReturn(Optional.of(product));

        Product result = productService.getProductById(id);

        assertEquals(product, result);
    }

    @Test
    void getProductById_ShouldThrow_WhenNotFound() {
        UUID id = UUID.randomUUID();
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> productService.getProductById(id));
    }

    @Test
    void getAllProducts_ShouldReturnList() {
        when(repository.findAll()).thenReturn(List.of(new Product()));

        List<Product> result = productService.getAllProducts();

        assertFalse(result.isEmpty());
    }

    @Test
    void updateProduct_ShouldUpdate_WhenExists() {
        UUID id = UUID.randomUUID();
        Product product = new Product();

        when(repository.existsById(id)).thenReturn(true);
        when(repository.save(any(Product.class))).thenReturn(product);

        Product result = productService.updateProduct(id, product);

        assertNotNull(result);
        assertEquals(id, product.getId());
        verify(repository).save(product);
    }

    @Test
    void updateProduct_ShouldThrow_WhenNotExists() {
        UUID id = UUID.randomUUID();
        Product product = new Product();
        when(repository.existsById(id)).thenReturn(false);

        assertThrows(NoSuchElementException.class, () -> productService.updateProduct(id, product));
    }

    @Test
    void deleteProduct_ShouldDelete_WhenExists() {
        UUID id = UUID.randomUUID();
        when(repository.existsById(id)).thenReturn(true);

        productService.deleteProduct(id);

        verify(repository).deleteById(id);
    }

    @Test
    void deleteProduct_ShouldDoNothing_WhenNotExists() {
        UUID id = UUID.randomUUID();
        when(repository.existsById(id)).thenReturn(false);

        productService.deleteProduct(id);

        verify(repository, never()).deleteById(id);
    }
}