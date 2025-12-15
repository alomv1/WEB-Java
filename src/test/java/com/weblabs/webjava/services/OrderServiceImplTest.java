package com.weblabs.webjava.services;

import com.weblabs.webjava.entity.Order;
import com.weblabs.webjava.entity.Product;
import com.weblabs.webjava.repository.OrderRepository;
import com.weblabs.webjava.services.impl.OrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void createOrder_ShouldSaveOrder() {
        UUID userId = UUID.randomUUID();
        Product p1 = new Product(UUID.randomUUID(), "P1", "Desc", 10.0, 5, null);

        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Order order = orderService.createOrder(userId, List.of(p1));

        assertNotNull(order);
        assertEquals(userId, order.getUserId());
        assertEquals("NEW", order.getStatus());
        assertEquals(1, order.getItems().size());
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void getOrderById_ShouldReturnOrder_WhenExists() {
        UUID orderId = UUID.randomUUID();
        Order order = new Order();
        order.setId(orderId);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        Order result = orderService.getOrderById(orderId);

        assertEquals(orderId, result.getId());
    }

    @Test
    void getOrderById_ShouldThrow_WhenNotFound() {
        UUID orderId = UUID.randomUUID();
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> orderService.getOrderById(orderId));
    }

    @Test
    void getOrdersByUserId_ShouldReturnList() {
        UUID userId = UUID.randomUUID();
        when(orderRepository.findByUserId(userId)).thenReturn(List.of(new Order(), new Order()));

        List<Order> orders = orderService.getOrdersByUserId(userId);

        assertEquals(2, orders.size());
        verify(orderRepository).findByUserId(userId);
    }

    @Test
    void changeOrderStatus_ShouldUpdateStatus() {
        UUID orderId = UUID.randomUUID();
        Order order = new Order();
        order.setId(orderId);
        order.setStatus("NEW");

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        orderService.changeOrderStatus(orderId, "SHIPPED");

        assertEquals("SHIPPED", order.getStatus());
        verify(orderRepository).save(order);
    }

    @Test
    void deleteOrder_ShouldCallDelete_WhenExists() {
        UUID orderId = UUID.randomUUID();
        when(orderRepository.existsById(orderId)).thenReturn(true);

        orderService.deleteOrder(orderId);

        verify(orderRepository).deleteById(orderId);
    }

    @Test
    void deleteOrder_ShouldNotCallDelete_WhenNotExists() {
        UUID orderId = UUID.randomUUID();
        when(orderRepository.existsById(orderId)).thenReturn(false);

        orderService.deleteOrder(orderId);

        verify(orderRepository, never()).deleteById(orderId);
    }
}