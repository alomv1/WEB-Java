package com.weblabs.webjava.mapper;

import com.weblabs.webjava.domain.Cart;
import com.weblabs.webjava.domain.CartItem;
import com.weblabs.webjava.dto.CartDTO;
import com.weblabs.webjava.dto.CartItemDTO;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CartMapper {

    @Mapping(target = "items", source = "items")
    CartDTO toDto(Cart cart);


    @Mapping(target = "productId", source = "product.id")
    CartItemDTO toCartItemDto(CartItem item);


    @Mapping(target = "product", ignore = true)
    CartItem toCartItem(CartItemDTO dto);

    List<CartItemDTO> toCartItemDtoList(List<CartItem> items);

    List<CartItem> toCartItemList(List<CartItemDTO> items);
}
