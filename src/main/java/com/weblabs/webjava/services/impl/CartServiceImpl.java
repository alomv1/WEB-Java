package com.weblabs.webjava.services.impl;

import com.weblabs.webjava.services.CartService;
import com.weblabs.webjava.domain.Cart;
import com.weblabs.webjava.domain.Product;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CartServiceImpl implements CartService {

    private final Map<UUID, Cart> cartRepo = new HashMap<>();

    @Override
    public Cart getCartByUserId(UUID userId) {
        return cartRepo.getOrDefault(userId, new Cart(UUID.randomUUID(), userId, new ArrayList<>()));
    }

    @Override
    public Cart addItemToCart(UUID userId, Product product, int quantity) {
        Cart cart = cartRepo.getOrDefault(userId, new Cart(UUID.randomUUID(), userId, new ArrayList<>()));
        cart.addItem(product, quantity);
        cartRepo.put(userId, cart);
        return cart;
    }

    @Override
    public Cart removeItemFromCart(UUID userId, UUID productId) {
        Cart cart = cartRepo.get(userId);
        if (cart == null) throw new NoSuchElementException("Cart not found for userId: " + userId);
        cart.removeItem(new Product(productId, null, null, 0, 0, null));
        return cart;
    }

    @Override
    public void clearCart(UUID userId) {
        Cart cart = cartRepo.get(userId);
        if (cart != null) cart.clear();
    }
}