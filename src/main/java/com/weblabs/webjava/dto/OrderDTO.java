package com.weblabs.webjava.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class OrderDTO {

    private UUID id;

    @NotNull(message = "userId must not be null")
    private UUID userId;

    @Valid
    @NotNull(message = "items must not be null")
    private List<OrderItemDTO> items;

    private String status;

    private LocalDateTime createdAt;

    public OrderDTO() {}

    public OrderDTO(UUID id, UUID userId, List<OrderItemDTO> items, String status, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.items = items;
        this.status = status;
        this.createdAt = createdAt;
    }

}
