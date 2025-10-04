package com.weblabs.webjava.domain;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OrderItem {
    private Product product;
    private int quantity;
    private double price;


    public OrderItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
        this.price = product.getPrice();
    }

}