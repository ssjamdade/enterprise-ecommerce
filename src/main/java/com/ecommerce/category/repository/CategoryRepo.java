package com.ecommerce.category.repository;

import com.ecommerce.category.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepo extends JpaRepository<CategoryEntity, Integer> {

    boolean existsByNameIgnoreCase(String name);
    Optional<CategoryEntity> findById(Long id);

}
