package com.weblabs.webjava.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weblabs.webjava.domain.Category;
import com.weblabs.webjava.dto.CategoryDTO;
import com.weblabs.webjava.mapper.CategoryMapper;
import com.weblabs.webjava.services.CategoryService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CategoryService categoryService;

    @MockitoBean
    private CategoryMapper categoryMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createCategory_Positive() throws Exception {
        CategoryDTO dto = new CategoryDTO();
        dto.setName("New Cat");

        Mockito.when(categoryMapper.toDomain(any())).thenReturn(new Category());
        Mockito.when(categoryService.createCategory(any())).thenReturn(new Category());
        Mockito.when(categoryMapper.toDto(any())).thenReturn(dto);

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    void getAllCategories_ShouldReturnList() throws Exception {
        Mockito.when(categoryService.getAllCategories()).thenReturn(List.of(new Category()));
        Mockito.when(categoryMapper.toDto(any())).thenReturn(new CategoryDTO());

        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk());
    }

    @Test
    void getCategoryById_ShouldReturnOk() throws Exception {
        UUID id = UUID.randomUUID();
        Mockito.when(categoryService.getCategoryById(id)).thenReturn(new Category());
        Mockito.when(categoryMapper.toDto(any())).thenReturn(new CategoryDTO());

        mockMvc.perform(get("/api/categories/{id}", id))
                .andExpect(status().isOk());
    }

    @Test
    void updateCategory_ShouldReturnOk() throws Exception {
        UUID id = UUID.randomUUID();
        CategoryDTO dto = new CategoryDTO();
        dto.setName("Updated");

        Mockito.when(categoryMapper.toDomain(any())).thenReturn(new Category());
        Mockito.when(categoryService.updateCategory(eq(id), any())).thenReturn(new Category());
        Mockito.when(categoryMapper.toDto(any())).thenReturn(dto);

        mockMvc.perform(put("/api/categories/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    void deleteCategory_ShouldReturnNoContent() throws Exception {
        UUID id = UUID.randomUUID();
        mockMvc.perform(delete("/api/categories/{id}", id))
                .andExpect(status().isNoContent());

        Mockito.verify(categoryService).deleteCategory(id);
    }
}