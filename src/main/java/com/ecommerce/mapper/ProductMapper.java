package com.ecommerce.mapper;

import com.ecommerce.product.dto.CreateProductRequest;
import com.ecommerce.product.dto.ProductResponse;
import com.ecommerce.product.entity.ProductEntity;
import com.ecommerce.product.entity.ProductImageEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "images", ignore = true)
    ProductEntity toEntity(CreateProductRequest request);

    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.name", target = "categoryName")
    @Mapping(target = "images", expression = "java(mapImages(entity.getImages()))")
    ProductResponse toResponse(ProductEntity entity);

    default List<String> mapImages(List<ProductImageEntity> images) {

        if (images == null) {
            return Collections.emptyList();
        }

        return images.stream()
                .map(ProductImageEntity::getImageUrl)
                .toList();
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "sku", ignore = true)
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "images", ignore = true)
    @Mapping(target = "inventory", ignore = true)
    void updateEntity(CreateProductRequest request, @MappingTarget ProductEntity entity);
}