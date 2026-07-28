package com.ecommerce.category.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class CategoryResponse {

    private Long id;
    private String name;
    private String description;
    private Boolean active;
}