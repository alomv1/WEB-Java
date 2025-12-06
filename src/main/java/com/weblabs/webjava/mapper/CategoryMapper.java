package com.weblabs.webjava.mapper;

import com.weblabs.webjava.dto.CategoryDTO;
import com.weblabs.webjava.domain.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryDTO toDto(Category category);

    Category toDomain(CategoryDTO categoryDTO);
}
