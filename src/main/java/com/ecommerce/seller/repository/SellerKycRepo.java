package com.ecommerce.seller.repository;

import com.ecommerce.seller.entity.SellerKycEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SellerKycRepo extends JpaRepository<SellerKycEntity, Long> {

    @EntityGraph(attributePaths = {"user"})
    Optional<SellerKycEntity> findByUserId(Long userId);

    @EntityGraph(attributePaths = {"user"})
    Optional<SellerKycEntity> findByUserEmail(String email);

    boolean existsByUserId(Long userId);

    @EntityGraph(attributePaths = {"user"})
    Page<SellerKycEntity> findAllByOverallStatus(String overallStatus, Pageable pageable);

    @EntityGraph(attributePaths = {"user"})
    Page<SellerKycEntity> findAll(Pageable pageable);
}
