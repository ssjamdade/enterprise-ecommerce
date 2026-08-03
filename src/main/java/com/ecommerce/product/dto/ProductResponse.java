package com.ecommerce.product.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
public class ProductResponse {

    private Long id;
    private String name;
    private String description;
    private String sku;
    private BigDecimal price;
    private Integer quantity;
    private Boolean active;

    private Long categoryId;
    private String categoryName;

    private List<String> images;
}
