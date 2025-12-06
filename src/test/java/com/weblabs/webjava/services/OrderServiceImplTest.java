package com.weblabs.webjava.services;

import com.weblabs.webjava.domain.Order;
import com.weblabs.webjava.domain.Product;
import com.weblabs.webjava.services.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OrderServiceImplTest {

    private OrderServiceImpl orderService;

    @BeforeEach
    void setUp() {
        orderService = new OrderServiceImpl();
    }

    @Test
    void createOrder_ShouldCreateNewOrder() {
        UUID userId = UUID.randomUUID();
        Product p1 = new Product(UUID.randomUUID(), "P1", "D", 10.0, 5, null);

        Order order = orderService.createOrder(userId, List.of(p1));

        assertNotNull(order.getId());
        assertEquals("NEW", order.getStatus());
        assertEquals(userId, order.getUserId());
    }

    @Test
    void changeOrderStatus_ShouldUpdateStatus() {
        UUID userId = UUID.randomUUID();
        Product p1 = new Product(UUID.randomUUID(), "P1", "D", 10.0, 5, null);
        Order order = orderService.createOrder(userId, List.of(p1));

        orderService.changeOrderStatus(order.getId(), "SHIPPED");

        Order updated = orderService.getOrderById(order.getId());
        assertEquals("SHIPPED", updated.getStatus());
    }

    @Test
    void getOrdersByUserId_ShouldReturnList() {
        UUID userId = UUID.randomUUID();
        Product p1 = new Product(UUID.randomUUID(), "P1", "D", 10.0, 5, null);

        orderService.createOrder(userId, List.of(p1));
        orderService.createOrder(userId, List.of(p1));

        List<Order> orders = orderService.getOrdersByUserId(userId);
        assertEquals(2, orders.size());
    }

    @Test
    void getOrderById_ShouldThrow_WhenMissing() {
        assertThrows(NoSuchElementException.class, () -> orderService.getOrderById(UUID.randomUUID()));
    }
}