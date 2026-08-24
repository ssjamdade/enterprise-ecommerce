package com.ecommerce.order.service.impl;

import com.ecommerce.address.entity.AddressEntity;
import com.ecommerce.address.repository.AddressRepo;
import com.ecommerce.auth.entity.UserEntity;
import com.ecommerce.cart.entity.CartEntity;
import com.ecommerce.cart.entity.CartItemEntity;
import com.ecommerce.cart.repository.CartRepo;
import com.ecommerce.common.exception.BadRequestException;
import com.ecommerce.common.exception.ResourceNotFoundException;
import com.ecommerce.mapper.OrderMapper;
import com.ecommerce.order.dto.CreateOrderRequest;
import com.ecommerce.order.dto.OrderResponse;
import com.ecommerce.order.entity.OrderEntity;
import com.ecommerce.order.entity.OrderItemEntity;
import com.ecommerce.order.repo.OrderRepo;
import com.ecommerce.order.service.OrderService;
import com.ecommerce.product.entity.InventoryEntity;
import com.ecommerce.product.entity.ProductEntity;
import com.ecommerce.product.repository.InventoryRepo;
import com.ecommerce.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderMapper orderMapper;
    private final SecurityUtils securityUtils;
    private final CartRepo cartRepo;
    private final InventoryRepo inventoryRepo;
    private final OrderRepo orderRepo;
    private final AddressRepo addressRepo;

    @Override
    @Transactional
    public OrderResponse createOrder(CreateOrderRequest request) {

        UserEntity user = securityUtils.getCurrentUser();

        AddressEntity address = addressRepo
                .findByIdAndUserId(request.getAddressId(), user.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Address not found."));

        CartEntity cart = cartRepo.findByUserId(user.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Cart not found."));

        if (cart.getItems().isEmpty()) {
            throw new BadRequestException("Cart is empty.");
        }

        OrderEntity order = OrderEntity.builder()
                .user(user)
                .shippingFullName(address.getFullName())
                .shippingPhone(address.getPhone())
                .shippingAddressLine(address.getAddressLine())
                .shippingCity(address.getCity())
                .shippingState(address.getState())
                .shippingPincode(address.getPincode())
                .shippingCountry(address.getCountry())
                .status(OrderEntity.OrderStatus.PENDING)
                .paymentStatus(OrderEntity.PaymentStatus.PENDING)
                .totalAmount(BigDecimal.ZERO)
                .build();

        BigDecimal total = BigDecimal.ZERO;

        for (CartItemEntity cartItem : cart.getItems()) {

            ProductEntity product = cartItem.getProduct();

            InventoryEntity inventory = inventoryRepo
                    .findByProductIdForUpdate(product.getId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Inventory not found for product: " + product.getId()));

            int availableQuantity =
                    inventory.getQuantity() - inventory.getReservedQuantity();

            if (availableQuantity < cartItem.getQuantity()) {
                throw new BadRequestException(
                        "Insufficient stock for product: " + product.getName());
            }

            inventory.setReservedQuantity(
                    inventory.getReservedQuantity() + cartItem.getQuantity()
            );

            BigDecimal price = product.getPrice();

            BigDecimal subtotal = price.multiply(
                    BigDecimal.valueOf(cartItem.getQuantity())
            );

            OrderItemEntity orderItem = OrderItemEntity.builder()
                    .order(order)
                    .product(product)
                    .productName(product.getName())
                    .price(price)
                    .quantity(cartItem.getQuantity())
                    .subtotal(subtotal)
                    .build();

            order.getItems().add(orderItem);

            total = total.add(subtotal);
        }

        order.setTotalAmount(total);

        orderRepo.save(order);

        cart.getItems().clear();

        return orderMapper.toResponse(order);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getById(Long id) {

        UserEntity user = securityUtils.getCurrentUser();

        OrderEntity order = orderRepo
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order not found."));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new BadRequestException("You cannot access this order.");
        }

        return orderMapper.toResponse(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getMyOrders() {

        UserEntity user = securityUtils.getCurrentUser();

        return orderRepo
                .findByUserIdOrderByCreatedAtDesc(user.getId())
                .stream()
                .map(orderMapper::toResponse)
                .toList();
    }
}
