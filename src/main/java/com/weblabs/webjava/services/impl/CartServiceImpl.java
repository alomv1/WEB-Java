package com.weblabs.webjava.services.impl;

import com.weblabs.webjava.domain.Cart;
import com.weblabs.webjava.domain.Product;
import com.weblabs.webjava.exception.PersistenceException;
import com.weblabs.webjava.repository.CartRepository;
import com.weblabs.webjava.services.CartService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@Transactional
public class CartServiceImpl implements CartService {

    private final CartRepository repository;

    public CartServiceImpl(CartRepository repository) {
        this.repository = repository;
    }

    @Override
    public Cart getCartByUserId(UUID userId) {
        try {
            return repository.findByUserId(userId)
                    .orElseGet(() -> {
                        Cart newCart = new Cart(null, userId, new ArrayList<>());
                        return repository.save(newCart);
                    });
        } catch (Exception e) {
            throw new PersistenceException("Failed to fetch or create cart for user: " + userId, e);
        }
    }

    @Override
    public Cart addItemToCart(UUID userId, Product product, int quantity) {
        try {
            Cart cart = getCartByUserId(userId);
            cart.addItem(product, quantity);
            return repository.save(cart);
        } catch (Exception e) {
            throw new PersistenceException("Failed to add item to cart for user: " + userId, e);
        }
    }

    @Override
    public Cart removeItemFromCart(UUID userId, UUID productId) {
        try {
            Cart cart = repository.findByUserId(userId)
                    .orElseThrow(() -> new NoSuchElementException("Cart not found for userId: " + userId));

            Product productToRemove = new Product();
            productToRemove.setId(productId);

            cart.removeItem(productToRemove);
            return repository.save(cart);
        } catch (NoSuchElementException e) {
            throw e;
        } catch (Exception e) {
            throw new PersistenceException("Failed to remove item from cart for user: " + userId, e);
        }
    }

    @Override
    public void clearCart(UUID userId) {
        try {
            repository.findByUserId(userId).ifPresent(cart -> {
                cart.clear();
                repository.save(cart);
            });
        } catch (Exception e) {
            throw new PersistenceException("Failed to clear cart for user: " + userId, e);
        }
    }
}