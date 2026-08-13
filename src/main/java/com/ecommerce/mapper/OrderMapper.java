package com.ecommerce.mapper;

import com.ecommerce.order.dto.OrderItemResponse;
import com.ecommerce.order.dto.OrderResponse;
import com.ecommerce.order.entity.OrderEntity;
import com.ecommerce.order.entity.OrderItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(source = "user.id", target = "userId")
    OrderResponse toResponse(OrderEntity entity);

    OrderItemResponse toItemResponse(OrderItemEntity entity);
}