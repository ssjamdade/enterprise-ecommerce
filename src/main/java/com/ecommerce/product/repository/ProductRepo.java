package com.ecommerce.product.repository;

import com.ecommerce.product.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProductRepo extends JpaRepository<ProductEntity, Long>,
        JpaSpecificationExecutor<ProductEntity> {

    boolean existsBySku(String sku);

    Page<ProductEntity> findAllBySellerId(Long sellerId, Pageable pageable);

}