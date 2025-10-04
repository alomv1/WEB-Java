package com.weblabs.webjava.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class Category {
    private UUID id;
    private String name;

    public Category() {}


}