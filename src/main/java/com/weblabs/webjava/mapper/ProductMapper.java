package com.weblabs.webjava.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.weblabs.webjava.domain.Product;
import com.weblabs.webjava.dto.ProductDTO;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "category", ignore = true)

    Product toDomain(ProductDTO dto);

    @Mapping(source = "category.id", target = "categoryId")

    ProductDTO toDto(Product product);
}