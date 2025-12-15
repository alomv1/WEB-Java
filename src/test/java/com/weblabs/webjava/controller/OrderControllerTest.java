package com.weblabs.webjava.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weblabs.webjava.entity.Order;
import com.weblabs.webjava.entity.Product;
import com.weblabs.webjava.dto.OrderDTO;
import com.weblabs.webjava.mapper.OrderMapper;
import com.weblabs.webjava.services.OrderService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    @MockitoBean
    private ProductService productService;

    @MockitoBean
    private OrderMapper orderMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createOrder_Positive() throws Exception {
        UUID userId = UUID.randomUUID();
        List<UUID> productIds = List.of(UUID.randomUUID(), UUID.randomUUID());

        Mockito.when(productService.getProductById(any())).thenReturn(new Product());
        Mockito.when(orderService.createOrder(any(), any())).thenReturn(new Order());
        Mockito.when(orderMapper.toDto(any())).thenReturn(new OrderDTO());

        mockMvc.perform(post("/api/orders/{userId}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productIds)))
                .andExpect(status().isCreated());
    }

    @Test
    void getOrder_Positive() throws Exception {
        UUID orderId = UUID.randomUUID();
        Mockito.when(orderService.getOrderById(orderId)).thenReturn(new Order());
        Mockito.when(orderMapper.toDto(any())).thenReturn(new OrderDTO());

        mockMvc.perform(get("/api/orders/{orderId}", orderId))
                .andExpect(status().isOk());
    }

    @Test
    void changeStatus_ShouldReturnNoContent() throws Exception {
        UUID orderId = UUID.randomUUID();

        mockMvc.perform(patch("/api/orders/{orderId}/status", orderId)
                        .param("status", "SHIPPED"))
                .andExpect(status().isNoContent());

        Mockito.verify(orderService).changeOrderStatus(orderId, "SHIPPED");
    }

    @Test
    void deleteOrder_ShouldReturnNoContent() throws Exception {
        UUID orderId = UUID.randomUUID();

        mockMvc.perform(delete("/api/orders/{orderId}", orderId))
                .andExpect(status().isNoContent());

        Mockito.verify(orderService).deleteOrder(orderId);
    }
}