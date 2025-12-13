package com.weblabs.webjava.services;

import com.weblabs.webjava.domain.Cart;
import com.weblabs.webjava.domain.Product;
import com.weblabs.webjava.repository.CartRepository;
import com.weblabs.webjava.services.impl.CartServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    @Test
    void getCartByUserId_ShouldReturnNewCart_IfNotExist() {
        UUID userId = UUID.randomUUID();
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.empty());
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Cart cart = cartService.getCartByUserId(userId);

        assertNotNull(cart);
        assertEquals(userId, cart.getUserId());
        verify(cartRepository).save(any(Cart.class));
    }

    @Test
    void addItemToCart_ShouldAddItem() {
        UUID userId = UUID.randomUUID();
        Cart existingCart = new Cart(UUID.randomUUID(), userId, new ArrayList<>());
        Product product = new Product(UUID.randomUUID(), "Item 1", "D", 50.0, 10, null);

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(existingCart));
        when(cartRepository.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Cart updatedCart = cartService.addItemToCart(userId, product, 2);

        assertFalse(updatedCart.getItems().isEmpty());
        assertEquals(1, updatedCart.getItems().size());
        verify(cartRepository).save(existingCart);
    }

    @Test
    void removeItemFromCart_ShouldThrow_IfCartNotFound() {
        UUID userId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () ->
                cartService.removeItemFromCart(userId, productId)
        );
    }

    @Test
    void clearCart_ShouldRemoveAllItems() {
        UUID userId = UUID.randomUUID();
        Cart cart = new Cart(UUID.randomUUID(), userId, new ArrayList<>());
        Product product = new Product(UUID.randomUUID(), "Item 1", "D", 50.0, 10, null);
        cart.addItem(product, 1);

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));

        cartService.clearCart(userId);

        assertTrue(cart.getItems().isEmpty());
        verify(cartRepository).save(cart);
    }
}