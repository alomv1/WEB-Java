package com.weblabs.webjava.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
public class Product {
    @Setter
    private UUID id;
    @Setter
    private String name;
    @Setter
    private String description;
    @Setter
    private double price;
    @Setter
    private int stock;
    private Category category;


    public Product() {
    }

    public Product(UUID id, String name, String description, double price, int stock, Category category) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.category = category;
    }

}