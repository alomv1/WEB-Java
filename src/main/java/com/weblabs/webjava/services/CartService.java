package com.weblabs.webjava.services;

import com.weblabs.webjava.domain.Cart;
import com.weblabs.webjava.domain.Product;

import java.util.UUID;

public interface CartService {

    Cart getCartByUserId(UUID userId);

    Cart addItemToCart(UUID userId, Product product, int quantity);

    Cart removeItemFromCart(UUID userId, UUID productId);

    void clearCart(UUID userId);
}