package com.weblabs.webjava.services;

import com.weblabs.webjava.domain.Order;
import com.weblabs.webjava.domain.Product;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    Order createOrder(UUID userId, List<Product> products);

    Order getOrderById(UUID orderId);

    List<Order> getOrdersByUserId(UUID userId);

    void changeOrderStatus(UUID orderId, String status);

    void deleteOrder(UUID orderId);
}