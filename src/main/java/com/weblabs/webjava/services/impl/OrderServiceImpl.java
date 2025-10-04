package com.weblabs.webjava.services.impl;

import com.weblabs.webjava.domain.Order;
import com.weblabs.webjava.domain.OrderItem;
import com.weblabs.webjava.domain.Product;
import com.weblabs.webjava.services.OrderService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {

    private final Map<UUID, Order> orderRepo = new HashMap<>();

    @Override
    public Order createOrder(UUID userId, List<Product> products) {
        UUID orderId = UUID.randomUUID();
        List<OrderItem> items = new ArrayList<>();
        for (Product p : products) {
            items.add(new OrderItem(p, 1));
        }
        Order order = new Order(orderId, userId, items, "NEW", LocalDateTime.now());
        orderRepo.put(orderId, order);
        return order;
    }

    @Override
    public Order getOrderById(UUID orderId) {
        Order order = orderRepo.get(orderId);
        if (order == null) throw new NoSuchElementException("Order not found with id: " + orderId);
        return order;
    }

    @Override
    public List<Order> getOrdersByUserId(UUID userId) {
        List<Order> orders = new ArrayList<>();
        for (Order order : orderRepo.values()) {
            if (order.getUserId().equals(userId)) {
                orders.add(order);
            }
        }
        return orders;
    }

    @Override
    public void changeOrderStatus(UUID orderId, String status) {
        Order order = orderRepo.get(orderId);
        if (order == null) throw new NoSuchElementException("Order not found with id: " + orderId);
        order.setStatus(status);
    }

    @Override
    public void deleteOrder(UUID orderId) {
        if (!orderRepo.containsKey(orderId)) {
            throw new NoSuchElementException("Order not found with id: " + orderId);
        }
        orderRepo.remove(orderId);
    }
}