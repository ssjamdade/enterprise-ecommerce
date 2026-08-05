package com.ecommerce.product.repository;

import com.ecommerce.product.entity.ProductImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImageRepo extends JpaRepository<ProductImageEntity, Long> {
}
