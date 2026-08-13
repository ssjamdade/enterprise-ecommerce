package com.ecommerce.order.dto;

import com.ecommerce.order.entity.OrderEntity;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
public class OrderResponse {

    private Long id;
    private Long userId;
    private BigDecimal totalAmount;
    private OrderEntity.OrderStatus status;
    private OrderEntity.PaymentStatus paymentStatus;
    private List<OrderItemResponse> items;
}