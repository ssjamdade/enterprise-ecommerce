package com.ecommerce.product.controller;

import com.ecommerce.common.response.ApiResponse;
import com.ecommerce.product.dto.CreateProductRequest;
import com.ecommerce.product.dto.ProductResponse;
import com.ecommerce.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponse>> create(
            @Valid @RequestBody CreateProductRequest request) {

        return ResponseEntity.ok(ApiResponse.<ProductResponse>builder()
                .success(true)
                .message("Product created successfully.")
                .data(productService.create(request))
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody CreateProductRequest request) {

        return ResponseEntity.ok(ApiResponse.<ProductResponse>builder()
                .success(true)
                .message("Product updated successfully.")
                .data(productService.update(id, request))
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {

        productService.delete(id);

        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .success(true)
                .message("Product deleted successfully.")
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponse>> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(ApiResponse.<ProductResponse>builder()
                .success(true)
                .message("Product fetched successfully.")
                .data(productService.getById(id))
                .build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ProductResponse>>> getAll(
            Pageable pageable) {

        return ResponseEntity.ok(ApiResponse.<Page<ProductResponse>>builder()
                .success(true)
                .message("Products fetched successfully.")
                .data(productService.getAll(pageable))
                .build());
    }
}