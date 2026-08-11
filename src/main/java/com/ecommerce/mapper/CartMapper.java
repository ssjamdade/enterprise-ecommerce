package com.ecommerce.mapper;

import com.ecommerce.cart.dto.CartItemResponse;
import com.ecommerce.cart.entity.CartItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartMapper {

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "product.price", target = "price")
    @Mapping(target = "subtotal", ignore = true)
    CartItemResponse toItemResponse(CartItemEntity entity);
}