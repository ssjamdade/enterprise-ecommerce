package com.ecommerce.cart.service.impl;

import com.ecommerce.auth.entity.UserEntity;
import com.ecommerce.auth.repository.UserRepo;
import com.ecommerce.cart.dto.AddToCartRequest;
import com.ecommerce.cart.dto.CartItemResponse;
import com.ecommerce.cart.dto.CartResponse;
import com.ecommerce.cart.dto.UpdateCartItemRequest;
import com.ecommerce.cart.entity.CartEntity;
import com.ecommerce.cart.entity.CartItemEntity;
import com.ecommerce.cart.repository.CartItemRepo;
import com.ecommerce.cart.repository.CartRepo;
import com.ecommerce.cart.service.CartService;
import com.ecommerce.common.exception.BadRequestException;
import com.ecommerce.common.exception.ResourceNotFoundException;
import com.ecommerce.mapper.CartMapper;
import com.ecommerce.product.entity.ProductEntity;
import com.ecommerce.product.repository.ProductRepo;
import com.ecommerce.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl implements CartService {

    private final CartRepo cartRepository;
    private final CartItemRepo cartItemRepository;
    private final ProductRepo productRepository;
    private final CartMapper cartMapper;
    private final UserRepo userRepository;
    private final SecurityUtils securityUtils;

    @Override
    @Transactional(readOnly = true)
    public CartResponse getCart() {

        UserEntity user = securityUtils.getCurrentUser();

        CartEntity cart = cartRepository.findByUserId(user.getId())
                .orElseGet(() -> createCart(user));

        return mapToResponse(cart);
    }

    @Override
    public CartResponse addItem(AddToCartRequest request) {

        UserEntity user = securityUtils.getCurrentUser();

        CartEntity cart = cartRepository.findByUserId(user.getId())
                .orElseGet(() -> createCart(user));

        ProductEntity product = productRepository.findById(request.getProductId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found."));

        if (!product.getActive()) {
            throw new BadRequestException("Product is inactive.");
        }

        CartItemEntity item = cartItemRepository
                .findByCartIdAndProductId(cart.getId(), product.getId())
                .orElse(null);

        if (item == null) {

            item = CartItemEntity.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(request.getQuantity())
                    .build();

        } else {

            item.setQuantity(
                    item.getQuantity() + request.getQuantity()
            );
        }

        cartItemRepository.save(item);

        return mapToResponse(cart);
    }

    @Override
    public CartResponse updateItem(
            Long itemId,
            UpdateCartItemRequest request) {

        UserEntity user = securityUtils.getCurrentUser();

        CartItemEntity item = cartItemRepository.findById(itemId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Cart item not found."));

        if (!item.getCart().getUser().getId().equals(user.getId())) {
            throw new BadRequestException("You cannot modify this cart item.");
        }

        item.setQuantity(request.getQuantity());

        cartItemRepository.save(item);

        return mapToResponse(item.getCart());
    }

    @Override
    public void removeItem(Long itemId) {

        UserEntity user = securityUtils.getCurrentUser();

        CartItemEntity item = cartItemRepository.findById(itemId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Cart item not found."));

        if (!item.getCart().getUser().getId().equals(user.getId())) {
            throw new BadRequestException("You cannot modify this cart item.");
        }

        cartItemRepository.delete(item);
    }

    @Override
    public void clearCart() {

        UserEntity user = securityUtils.getCurrentUser();

        CartEntity cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Cart not found."));

        cart.getItems().clear();

        cartRepository.save(cart);
    }

    private CartEntity createCart(UserEntity user) {

        CartEntity cart = CartEntity.builder()
                .user(user)
                .build();

        return cartRepository.save(cart);
    }

    private CartResponse mapToResponse(CartEntity cart) {

        List<CartItemResponse> items = cart.getItems()
                .stream()
                .map(item -> {

                    CartItemResponse response =
                            cartMapper.toItemResponse(item);

                    BigDecimal subtotal = item.getProduct()
                            .getPrice()
                            .multiply(BigDecimal.valueOf(item.getQuantity()));

                    response.setSubtotal(subtotal);

                    return response;
                })
                .toList();

        BigDecimal total = items.stream()
                .map(CartItemResponse::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return CartResponse.builder()
                .id(cart.getId())
                .items(items)
                .totalAmount(total)
                .build();
    }
}