package com.weblabs.webjava.domain;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class DomainEntitiesTest {

    @Test
    void testProductEntity() {
        UUID id = UUID.randomUUID();
        Product p = new Product();
        p.setId(id);
        p.setName("Test Product");
        p.setPrice(100.0);
        p.setStock(10);
        p.setDescription("Desc");

        Category cat = new Category();
        p.setCategory(cat);

        assertEquals(id, p.getId());
        assertEquals("Test Product", p.getName());
        assertEquals(100.0, p.getPrice());
        assertEquals(10, p.getStock());
        assertEquals("Desc", p.getDescription());
        assertEquals(cat, p.getCategory());

        Product p2 = new Product(id, "Name", "Desc", 50.0, 5, cat);
        assertNotNull(p2);

        assertNotEquals(p, new Product());
    }

    @Test
    void testCategoryEntity() {
        UUID id = UUID.randomUUID();
        Category c = new Category();
        c.setId(id);
        c.setName("Electronics");

        assertEquals(id, c.getId());
        assertEquals("Electronics", c.getName());

        Category c2 = new Category(id, "Electronics");
        assertNotNull(c2);
    }

    @Test
    void testOrderEntity() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();

        Order o = new Order();
        o.setId(id);
        o.setUserId(userId);
        o.setStatus("NEW");
        o.setItems(new ArrayList<>());
        o.setCreatedAt(now);

        assertEquals(id, o.getId());
        assertEquals(userId, o.getUserId());
        assertEquals("NEW", o.getStatus());
        assertNotNull(o.getItems());
        assertEquals(now, o.getCreatedAt());

        Order o2 = new Order(id, userId, new ArrayList<>(), "NEW", now);
        assertNotNull(o2);
    }

    @Test
    void testCartEntity() {
        UUID id = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Cart cart = new Cart();
        cart.setId(id);
        cart.setUserId(userId);
        cart.setItems(new ArrayList<>());

        assertEquals(id, cart.getId());
        assertEquals(userId, cart.getUserId());

        Cart cart2 = new Cart(id, userId, new ArrayList<>());
        assertNotNull(cart2);

        Product product = new Product();
        product.setId(UUID.randomUUID());

        cart.addItem(product, 2);
        assertFalse(cart.getItems().isEmpty());
        assertEquals(2, cart.getItems().get(0).getQuantity());

        cart.removeItem(product);
        assertTrue(cart.getItems().isEmpty());
    }

    @Test
    void testCartItemEntity() {
        UUID id = UUID.randomUUID();
        Product product = new Product();
        Cart cart = new Cart();

        CartItem item = new CartItem();
        item.setId(id);
        item.setProduct(product);
        item.setQuantity(5);
        item.setCart(cart);

        assertEquals(id, item.getId());
        assertEquals(product, item.getProduct());
        assertEquals(5, item.getQuantity());
        assertEquals(cart, item.getCart());

        CartItem itemCustom = new CartItem(product, 10);
        assertEquals(10, itemCustom.getQuantity());

        CartItem itemAll = new CartItem(id, product, 5, cart);
        assertNotNull(itemAll);
    }

    @Test
    void testOrderItemEntity() {
        UUID id = UUID.randomUUID();
        Product product = new Product();
        product.setPrice(50.0);
        Order order = new Order();

        OrderItem item = new OrderItem();
        item.setId(id);
        item.setProduct(product);
        item.setQuantity(2);
        item.setPrice(50.0);
        item.setOrder(order);

        assertEquals(id, item.getId());
        assertEquals(product, item.getProduct());
        assertEquals(50.0, item.getPrice());
        assertEquals(order, item.getOrder());

        OrderItem itemCustom = new OrderItem(product, 3);
        assertEquals(3, itemCustom.getQuantity());
        assertEquals(50.0, itemCustom.getPrice());
    }
}