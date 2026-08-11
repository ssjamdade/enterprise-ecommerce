package com.ecommerce.cart.repository;

import com.ecommerce.cart.entity.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepo extends JpaRepository<CartEntity, Long> {

    Optional<CartEntity> findByUserId(Long userId);

}
