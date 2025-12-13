package com.weblabs.webjava.services.impl;

import com.weblabs.webjava.domain.Order;
import com.weblabs.webjava.domain.OrderItem;
import com.weblabs.webjava.domain.Product;
import com.weblabs.webjava.exception.PersistenceException;
import com.weblabs.webjava.repository.OrderRepository;
import com.weblabs.webjava.services.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository repository;

    public OrderServiceImpl(OrderRepository repository) {
        this.repository = repository;
    }

    @Override
    public Order createOrder(UUID userId, List<Product> products) {
        try {
            List<OrderItem> items = new ArrayList<>();
            for (Product p : products) {
                items.add(new OrderItem(p, 1));
            }

            Order order = new Order(null, userId, items, "NEW", LocalDateTime.now());

            for (OrderItem item : items) {
                item.setOrder(order);
            }


            return repository.save(order);
        } catch (Exception e) {
            throw new PersistenceException("Failed to create order", e);
        }
    }

    @Override
    public Order getOrderById(UUID orderId) {
        try {
            return repository.findById(orderId)
                    .orElseThrow(() -> new NoSuchElementException("Order not found with id: " + orderId));
        } catch (NoSuchElementException e) {
            throw e;
        } catch (Exception e) {
            throw new PersistenceException("Failed to fetch order with id: " + orderId, e);
        }
    }

    @Override
    public List<Order> getOrdersByUserId(UUID userId) {
        try {
            return repository.findByUserId(userId);
        } catch (Exception e) {
            throw new PersistenceException("Failed to fetch orders for user: " + userId, e);
        }
    }

    @Override
    public void changeOrderStatus(UUID orderId, String status) {
        try {
            Order order = getOrderById(orderId);
            order.setStatus(status);
            repository.save(order);
        } catch (NoSuchElementException e) {
            throw e;
        } catch (Exception e) {
            throw new PersistenceException("Failed to update order status for id: " + orderId, e);
        }
    }

    @Override
    public void deleteOrder(UUID orderId) {
        try {
            if (repository.existsById(orderId)) {
                repository.deleteById(orderId);
            }
        } catch (Exception e) {
            throw new PersistenceException("Failed to delete order with id: " + orderId, e);
        }
    }
}