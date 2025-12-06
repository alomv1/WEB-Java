package com.weblabs.webjava.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weblabs.webjava.domain.Product;
import com.weblabs.webjava.dto.ProductDTO;
import com.weblabs.webjava.mapper.ProductMapper;
import com.weblabs.webjava.services.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private ProductMapper productMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createProduct_Positive() throws Exception {
        ProductDTO inputDto = new ProductDTO();
        inputDto.setName("Cosmic Product Name");
        inputDto.setPrice(100.0);
        inputDto.setStock(10);
        inputDto.setCategoryId(UUID.randomUUID());
        inputDto.setDescription("Description");

        Product domainProduct = new Product();
        Product savedProduct = new Product();
        savedProduct.setId(UUID.randomUUID());

        Mockito.when(productMapper.toDomain(any())).thenReturn(domainProduct);
        Mockito.when(productService.createProduct(any())).thenReturn(savedProduct);
        Mockito.when(productMapper.toDto(any())).thenReturn(inputDto);

        var result = mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andReturn();

    }

    @Test
    void getAllProducts_ShouldReturnList() throws Exception {
        Mockito.when(productService.getAllProducts()).thenReturn(List.of(new Product()));
        Mockito.when(productMapper.toDto(any())).thenReturn(new ProductDTO());

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk());
    }

    @Test
    void getProductById_Positive() throws Exception {
        UUID id = UUID.randomUUID();
        Product product = new Product();
        ProductDTO dto = new ProductDTO();

        Mockito.when(productService.getProductById(id)).thenReturn(product);
        Mockito.when(productMapper.toDto(product)).thenReturn(dto);

        mockMvc.perform(get("/api/products/{id}", id))
                .andExpect(status().isOk());
    }

    @Test
    void updateProduct_ShouldReturnOk() throws Exception {
        UUID id = UUID.randomUUID();

        ProductDTO inputDto = new ProductDTO(id, "Cosmic Updated Name", "Desc", 150.0, 5, UUID.randomUUID());

        Mockito.when(productMapper.toDomain(any())).thenReturn(new Product());
        Mockito.when(productService.updateProduct(eq(id), any())).thenReturn(new Product());
        Mockito.when(productMapper.toDto(any())).thenReturn(inputDto);

        mockMvc.perform(put("/api/products/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteProduct_ShouldReturnNoContent() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/api/products/{id}", id))
                .andExpect(status().isNoContent());

        Mockito.verify(productService).deleteProduct(id);
    }

}

