package com.weblabs.webjava.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
public class CategoryDTO {

    private UUID id;

    @NotNull(message = "name must not be null")
    @Size(min = 2, max = 100, message = "name length must be between 2 and 100")
    private String name;

    public CategoryDTO() {}
}