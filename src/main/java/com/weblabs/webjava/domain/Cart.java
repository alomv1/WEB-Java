package com.weblabs.webjava.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Setter
@Getter
public class Cart {
    private UUID id;
    private UUID userId;
    private List<CartItem> items;


    public Cart(UUID id, UUID userId, List<CartItem> items) {
        this.id = id;
        this.userId = userId;
        this.items = items;
    }

    public void addItem(Product product, int quantity) {
        CartItem existing = items.stream()
                .filter(i -> i.getProduct().getId().equals(product.getId()))
                .findFirst().orElse(null);
        if(existing != null) {
            existing.setQuantity(existing.getQuantity() + quantity);
        } else {
            items.add(new CartItem(product, quantity));
        }
    }

    public void removeItem(Product product) {
        items.removeIf(i -> i.getProduct().getId().equals(product.getId()));
    }

    public void clear() {
        items.clear();
    }
}
