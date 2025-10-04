package com.weblabs.webjava.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class CartDTO {

    private UUID id;

    @NotNull(message = "userId must not be null")
    private UUID userId;

    @Valid
    @NotNull(message = "items must not be null")
    private List<CartItemDTO> items;

    public CartDTO() {}
}