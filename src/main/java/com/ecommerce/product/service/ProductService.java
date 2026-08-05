package com.ecommerce.product.service;

import com.ecommerce.product.dto.CreateProductRequest;
import com.ecommerce.product.dto.ProductFilterRequest;
import com.ecommerce.product.dto.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {

    ProductResponse create(CreateProductRequest request);

    ProductResponse update(Long id, CreateProductRequest request);

    void delete(Long id);

    ProductResponse getById(Long id);

    Page<ProductResponse> getAll(Pageable pageable);

    Page<ProductResponse> search(ProductFilterRequest request,
                                 Pageable pageable);

    ProductResponse uploadImages(Long productId, MultipartFile[] files);

    void deleteImage(Long imageId);

    void setPrimaryImage(Long imageId);

}