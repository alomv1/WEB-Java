package com.weblabs.webjava.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
public class Order {
    private UUID id;
    private UUID userId;
    private List<OrderItem> items;
    private String status;
    private LocalDateTime createdAt;


    public Order() {}

    public Order(UUID id, UUID userId, List<OrderItem> items, String status, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.items = items;
        this.status = status;
        this.createdAt = createdAt;
    }

}