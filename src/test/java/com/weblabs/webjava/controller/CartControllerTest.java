package com.weblabs.webjava.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weblabs.webjava.domain.Cart;
import com.weblabs.webjava.domain.Product;
import com.weblabs.webjava.dto.CartDTO;
import com.weblabs.webjava.dto.CartItemDTO;
import com.weblabs.webjava.mapper.CartMapper;
import com.weblabs.webjava.services.CartService;
import com.weblabs.webjava.services.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CartController.class)
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CartService cartService;

    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private CartMapper cartMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void addItemToCart_Positive() throws Exception {
        UUID userId = UUID.randomUUID();
        CartItemDTO itemDTO = new CartItemDTO();
        itemDTO.setProductId(UUID.randomUUID());
        itemDTO.setQuantity(5);

        Mockito.when(productService.getProductById(any())).thenReturn(new Product());
        Mockito.when(cartService.addItemToCart(any(), any(), any(Integer.class))).thenReturn(new Cart());
        Mockito.when(cartMapper.toDto(any())).thenReturn(new CartDTO());

        mockMvc.perform(post("/api/carts/{userId}/items", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDTO)))
                .andExpect(status().isOk());
    }

    @Test
    void getCart_ShouldReturnOk() throws Exception {
        UUID userId = UUID.randomUUID();
        Mockito.when(cartService.getCartByUserId(userId)).thenReturn(new Cart());
        Mockito.when(cartMapper.toDto(any())).thenReturn(new CartDTO());

        mockMvc.perform(get("/api/carts/{userId}", userId))
                .andExpect(status().isOk());
    }

    @Test
    void clearCart_ShouldReturnNoContent() throws Exception {
        UUID userId = UUID.randomUUID();

        mockMvc.perform(delete("/api/carts/{userId}/clear", userId))
                .andExpect(status().isNoContent());

        Mockito.verify(cartService).clearCart(userId);
    }
}