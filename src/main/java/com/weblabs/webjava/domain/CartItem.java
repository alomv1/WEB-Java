package com.weblabs.webjava.domain;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CartItem {
    private Product product;
    private int quantity;

    public CartItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

}
