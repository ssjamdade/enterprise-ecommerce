package com.ecommerce.product.specification;

import com.ecommerce.product.dto.ProductFilterRequest;
import com.ecommerce.product.entity.ProductEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public final class ProductSpecification {

    private ProductSpecification() {
    }

    public static Specification<ProductEntity> filter(ProductFilterRequest request) {

        return (root, query, cb) -> {

            List<jakarta.persistence.criteria.Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(request.getKeyword())) {

                String keyword = "%" + request.getKeyword().toLowerCase() + "%";

                predicates.add(
                        cb.or(
                                cb.like(cb.lower(root.get("name")), keyword),
                                cb.like(cb.lower(root.get("description")), keyword),
                                cb.like(cb.lower(root.get("sku")), keyword)
                        )
                );
            }

            if (request.getCategoryId() != null) {

                predicates.add(
                        cb.equal(root.get("category").get("id"),
                                request.getCategoryId())
                );
            }

            if (request.getMinPrice() != null) {

                predicates.add(
                        cb.greaterThanOrEqualTo(root.get("price"),
                                request.getMinPrice())
                );
            }

            if (request.getMaxPrice() != null) {

                predicates.add(
                        cb.lessThanOrEqualTo(root.get("price"),
                                request.getMaxPrice())
                );
            }

            if (request.getActive() != null) {

                predicates.add(
                        cb.equal(root.get("active"),
                                request.getActive())
                );
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}