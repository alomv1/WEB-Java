package com.weblabs.webjava.services;

import com.weblabs.webjava.domain.Cart;
import com.weblabs.webjava.domain.Product;
import com.weblabs.webjava.exception.PersistenceException;
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
    void getCartByUserId_ShouldReturnExistingCart() {
        UUID userId = UUID.randomUUID();
        Cart cart = new Cart();
        cart.setUserId(userId);

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));

        Cart result = cartService.getCartByUserId(userId);
        assertEquals(userId, result.getUserId());
    }

    @Test
    void getCartByUserId_ShouldCreateNewCart_WhenNoneExists() {
        UUID userId = UUID.randomUUID();
        when(cartRepository.findByUserId(userId)).thenReturn(Optional.empty());
        when(cartRepository.save(any(Cart.class))).thenAnswer(inv -> inv.getArgument(0));

        Cart result = cartService.getCartByUserId(userId);

        assertNotNull(result);
        assertEquals(userId, result.getUserId());
        assertNotNull(result.getItems());
        verify(cartRepository).save(any(Cart.class));
    }

    @Test
    void getCartByUserId_ShouldThrowPersistenceException_OnDbError() {
        UUID userId = UUID.randomUUID();
        when(cartRepository.findByUserId(userId)).thenThrow(new RuntimeException("DB Connection failed"));

        assertThrows(PersistenceException.class, () -> cartService.getCartByUserId(userId));
    }


    @Test
    void addItemToCart_ShouldAddItem() {
        UUID userId = UUID.randomUUID();
        Cart cart = new Cart(UUID.randomUUID(), userId, new ArrayList<>());
        Product product = new Product(UUID.randomUUID(), "Item", "Desc", 100.0, 10, null);

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        cartService.addItemToCart(userId, product, 2);

        assertFalse(cart.getItems().isEmpty());
        verify(cartRepository).save(cart);
    }

    @Test
    void addItemToCart_ShouldThrowPersistenceException_OnError() {
        UUID userId = UUID.randomUUID();
        Product product = new Product();

        when(cartRepository.findByUserId(userId)).thenThrow(new RuntimeException("DB Error"));

        assertThrows(PersistenceException.class, () -> cartService.addItemToCart(userId, product, 1));
    }


    @Test
    void removeItemFromCart_ShouldRemoveItem() {
        UUID userId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();
        Cart cart = new Cart(UUID.randomUUID(), userId, new ArrayList<>());

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        cartService.removeItemFromCart(userId, productId);

        verify(cartRepository).save(cart);
    }

    @Test
    void removeItemFromCart_ShouldThrowNoSuchElement_WhenCartNotFound() {
        UUID userId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class,
                () -> cartService.removeItemFromCart(userId, productId));
    }

    @Test
    void removeItemFromCart_ShouldThrowPersistenceException_OnDbError() {
        UUID userId = UUID.randomUUID();
        UUID productId = UUID.randomUUID();

        when(cartRepository.findByUserId(userId)).thenThrow(new RuntimeException("DB Error"));

        assertThrows(PersistenceException.class,
                () -> cartService.removeItemFromCart(userId, productId));
    }


    @Test
    void clearCart_ShouldClearItems() {
        UUID userId = UUID.randomUUID();
        Cart cart = new Cart(UUID.randomUUID(), userId, new ArrayList<>());

        when(cartRepository.findByUserId(userId)).thenReturn(Optional.of(cart));

        cartService.clearCart(userId);

        assertTrue(cart.getItems().isEmpty());
        verify(cartRepository).save(cart);
    }

    @Test
    void clearCart_ShouldThrowPersistenceException_OnError() {
        UUID userId = UUID.randomUUID();
        when(cartRepository.findByUserId(userId)).thenThrow(new RuntimeException("DB Error"));

        assertThrows(PersistenceException.class, () -> cartService.clearCart(userId));
    }
}