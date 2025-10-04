package com.weblabs.webjava.dto;

import com.weblabs.webjava.validation.CosmicWordCheck;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ProductDTO {

    private UUID id;

    @NotNull(message = "name must not be null")
    @Size(min = 3, max = 255, message = "name length must be between 3 and 255")
    @CosmicWordCheck
    private String name;

    @Size(max = 2000, message = "description length must be less than 2000")
    private String description;

    @NotNull(message = "price must not be null")
    @Min(value = 1, message = "price must be greater than 0")
    private Double price;

    @NotNull(message = "stock must not be null")
    @Min(value = 0, message = "stock must be >= 0")
    private Integer stock;

    @NotNull(message = "categoryId must not be null")
    private UUID categoryId;

    public ProductDTO() {}

    public ProductDTO(UUID id, String name, String description, Double price, Integer stock, UUID categoryId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.categoryId = categoryId;
    }

}