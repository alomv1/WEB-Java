package com.weblabs.webjava.services;

import com.weblabs.webjava.domain.Cart;
import com.weblabs.webjava.domain.Product;
import com.weblabs.webjava.services.impl.CartServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CartServiceImplTest {

    private CartServiceImpl cartService;
    private UUID userId;

    @BeforeEach
    void setUp() {
        cartService = new CartServiceImpl();
        userId = UUID.randomUUID();
    }

    @Test
    void getCartByUserId_ShouldReturnNewCart_IfNotExist() {
        Cart cart = cartService.getCartByUserId(userId);
        assertNotNull(cart);
        assertEquals(userId, cart.getUserId());
        assertNotNull(cart.getItems());
    }

    @Test
    void addItemToCart_ShouldAddItem() {
        Product product = new Product(UUID.randomUUID(), "Item 1", "D", 50.0, 10, null);
        cartService.addItemToCart(userId, product, 2);

        Cart cart = cartService.getCartByUserId(userId);
        assertFalse(cart.getItems().isEmpty());
    }

    @Test
    void removeItemFromCart_ShouldThrow_IfCartNotFound() {
        assertThrows(NoSuchElementException.class, () ->
                cartService.removeItemFromCart(UUID.randomUUID(), UUID.randomUUID())
        );
    }

    @Test
    void clearCart_ShouldRemoveAllItems() {
        Product product = new Product(UUID.randomUUID(), "Item 1", "D", 50.0, 10, null);
        cartService.addItemToCart(userId, product, 1);

        cartService.clearCart(userId);

        Cart cart = cartService.getCartByUserId(userId);
        assertTrue(cart.getItems().isEmpty());
    }
}