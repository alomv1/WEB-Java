package com.weblabs.webjava.services;

import com.weblabs.webjava.domain.Order;
import com.weblabs.webjava.domain.Product;
import com.weblabs.webjava.repository.OrderRepository;
import com.weblabs.webjava.services.impl.OrderServiceImpl;
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
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    @Test
    void createOrder_ShouldCreateNewOrder() {
        UUID userId = UUID.randomUUID();
        Product p1 = new Product(UUID.randomUUID(), "P1", "D", 10.0, 5, null);

        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order saved = invocation.getArgument(0);
            saved.setId(UUID.randomUUID());
            return saved;
        });

        Order order = orderService.createOrder(userId, List.of(p1));

        assertNotNull(order.getId());
        assertEquals("NEW", order.getStatus());
        assertEquals(userId, order.getUserId());
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void changeOrderStatus_ShouldUpdateStatus() {
        UUID orderId = UUID.randomUUID();
        Order order = new Order();
        order.setId(orderId);
        order.setStatus("NEW");

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        orderService.changeOrderStatus(orderId, "SHIPPED");

        assertEquals("SHIPPED", order.getStatus());
        verify(orderRepository).save(order);
    }

    @Test
    void getOrdersByUserId_ShouldReturnList() {
        UUID userId = UUID.randomUUID();
        Order o1 = new Order();
        Order o2 = new Order();

        when(orderRepository.findByUserId(userId)).thenReturn(List.of(o1, o2));

        List<Order> orders = orderService.getOrdersByUserId(userId);

        assertEquals(2, orders.size());
        verify(orderRepository).findByUserId(userId);
    }

    @Test
    void getOrderById_ShouldThrow_WhenMissing() {
        UUID id = UUID.randomUUID();
        when(orderRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> orderService.getOrderById(id));
    }
}