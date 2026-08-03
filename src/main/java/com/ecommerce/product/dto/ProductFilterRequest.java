package com.ecommerce.product.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ProductFilterRequest {

    private String keyword;

    private Long categoryId;

    private BigDecimal minPrice;

    private BigDecimal maxPrice;

    private Boolean active;
}