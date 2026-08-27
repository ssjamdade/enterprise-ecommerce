package com.ecommerce.address.repository;

import com.ecommerce.address.entity.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AddressRepo extends JpaRepository<AddressEntity, Long> {

    List<AddressEntity> findByUserIdOrderByIsDefaultDescCreatedAtDesc(
            Long userId
    );

    Optional<AddressEntity> findByIdAndUserId(Long id, Long userId);

    boolean existsByUserId(Long userId);
}