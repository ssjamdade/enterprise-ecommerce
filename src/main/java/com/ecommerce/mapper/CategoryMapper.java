package com.ecommerce.mapper;

import com.ecommerce.category.dto.CategoryResponse;
import com.ecommerce.category.dto.CreateCategoryRequest;
import com.ecommerce.category.entity.CategoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryEntity toEntity(CreateCategoryRequest request);

    CategoryResponse toResponse(CategoryEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntity(CreateCategoryRequest request,
                      @MappingTarget CategoryEntity entity);
}