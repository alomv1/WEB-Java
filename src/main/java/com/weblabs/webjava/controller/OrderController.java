package com.weblabs.webjava.controller;

import com.weblabs.webjava.dto.OrderDTO;
import com.weblabs.webjava.mapper.OrderMapper;
import com.weblabs.webjava.domain.Order;
import com.weblabs.webjava.domain.Product;
import com.weblabs.webjava.services.OrderService;
import com.weblabs.webjava.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final ProductService productService;
    private final OrderMapper orderMapper;

    public OrderController(OrderService orderService, ProductService productService, OrderMapper orderMapper) {
        this.orderService = orderService;
        this.productService = productService;
        this.orderMapper = orderMapper;
    }

    @PostMapping("/{userId}")
    public ResponseEntity<OrderDTO> createOrder(@PathVariable UUID userId,
                                                @Valid @RequestBody List<UUID> productIds) {
        List<Product> products = productIds.stream()
                .map(productService::getProductById)
                .collect(Collectors.toList());
        Order order = orderService.createOrder(userId, products);
        return new ResponseEntity<>(orderMapper.toDto(order), HttpStatus.CREATED);
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDTO> getOrder(@PathVariable UUID orderId) {
        Order order = orderService.getOrderById(orderId);
        return ResponseEntity.ok(orderMapper.toDto(order));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderDTO>> getUserOrders(@PathVariable UUID userId) {
        List<OrderDTO> orders = orderService.getOrdersByUserId(userId)
                .stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(orders);
    }

    @PatchMapping("/{orderId}/status")
    public ResponseEntity<Void> changeStatus(@PathVariable UUID orderId,
                                             @RequestParam String status) {
        orderService.changeOrderStatus(orderId, status);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable UUID orderId) {
        orderService.deleteOrder(orderId);
        return ResponseEntity.noContent().build();
    }
}