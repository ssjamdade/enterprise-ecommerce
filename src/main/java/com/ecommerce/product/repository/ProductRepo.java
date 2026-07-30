package com.ecommerce.product.repository;

import com.ecommerce.product.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepo extends JpaRepository<ProductEntity, Long> {

    boolean existsBySku(String sku);

}