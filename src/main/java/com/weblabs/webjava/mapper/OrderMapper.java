package com.weblabs.webjava.mapper;

import com.weblabs.webjava.domain.Order;
import com.weblabs.webjava.domain.OrderItem;
import com.weblabs.webjava.dto.OrderItemDTO;
import com.weblabs.webjava.dto.OrderDTO;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "items", source = "items", qualifiedByName = "toOrderItemDTOList")
    OrderDTO toDto(Order order);

    @Mapping(target = "items", ignore = true)
    Order toDomain(OrderDTO dto);

    @Named("toOrderItemDTOList")
    default List<OrderItemDTO> toOrderItemDTOList(List<OrderItem> items) {
        if (items == null) return null;
        return items.stream().map(this::toOrderItemDTO).collect(Collectors.toList());
    }

    default OrderItemDTO toOrderItemDTO(OrderItem item) {
        if (item == null) return null;
        OrderItemDTO dto = new OrderItemDTO();
        dto.setProductId(item.getProduct().getId());
        dto.setProductName(item.getProduct().getName());
        dto.setQuantity(item.getQuantity());
        dto.setPrice(item.getPrice());
        return dto;
    }
}