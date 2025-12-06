package com.weblabs.webjava.controller;

import com.weblabs.webjava.dto.CartDTO;
import com.weblabs.webjava.dto.CartItemDTO;
import com.weblabs.webjava.mapper.CartMapper;
import com.weblabs.webjava.domain.Cart;
import com.weblabs.webjava.domain.Product;
import com.weblabs.webjava.services.CartService;
import com.weblabs.webjava.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    private final CartService cartService;
    private final ProductService productService;
    private final CartMapper cartMapper;

    public CartController(CartService cartService, ProductService productService, CartMapper cartMapper) {
        this.cartService = cartService;
        this.productService = productService;
        this.cartMapper = cartMapper;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<CartDTO> getCart(@PathVariable UUID userId) {
        Cart cart = cartService.getCartByUserId(userId);
        return ResponseEntity.ok(cartMapper.toDto(cart));
    }

    @PostMapping("/{userId}/items")
    public ResponseEntity<CartDTO> addItem(@PathVariable UUID userId,
                                           @Valid @RequestBody CartItemDTO itemDTO) {
        Product product = productService.getProductById(itemDTO.getProductId());
        Cart cart = cartService.addItemToCart(userId, product, itemDTO.getQuantity());
        return ResponseEntity.ok(cartMapper.toDto(cart));
    }

    @DeleteMapping("/{userId}/items/{productId}")
    public ResponseEntity<CartDTO> removeItem(@PathVariable UUID userId, @PathVariable UUID productId) {
        Cart cart = cartService.removeItemFromCart(userId, productId);
        return ResponseEntity.ok(cartMapper.toDto(cart));
    }

    @DeleteMapping("/{userId}/clear")
    public ResponseEntity<Void> clearCart(@PathVariable UUID userId) {
        cartService.clearCart(userId);
        return ResponseEntity.noContent().build();
    }
}