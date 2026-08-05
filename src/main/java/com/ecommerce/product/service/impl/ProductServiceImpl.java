package com.ecommerce.product.service.impl;

import com.ecommerce.category.entity.CategoryEntity;
import com.ecommerce.category.repository.CategoryRepo;
import com.ecommerce.common.exception.ResourceAlreadyExistsException;
import com.ecommerce.common.exception.ResourceNotFoundException;
import com.ecommerce.mapper.ProductMapper;
import com.ecommerce.product.dto.CreateProductRequest;
import com.ecommerce.product.dto.ProductFilterRequest;
import com.ecommerce.product.dto.ProductResponse;
import com.ecommerce.product.entity.InventoryEntity;
import com.ecommerce.product.entity.ProductEntity;
import com.ecommerce.product.entity.ProductImageEntity;
import com.ecommerce.product.repository.ProductImageRepo;
import com.ecommerce.product.repository.ProductRepo;
import com.ecommerce.product.service.ImageService;
import com.ecommerce.product.service.ProductService;
import com.ecommerce.product.specification.ProductSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepo productRepository;
    private final CategoryRepo categoryRepository;
    private final ProductMapper productMapper;
    private final ImageService imageService;
    private final ProductImageRepo productImageRepo;

    @Override
    public ProductResponse create(CreateProductRequest request) {

        if (productRepository.existsBySku(request.getSku())) {
            throw new ResourceAlreadyExistsException("SKU already exists.");
        }

        CategoryEntity category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found."));

        ProductEntity product = productMapper.toEntity(request);

        product.setCategory(category);

        productRepository.save(product);

        return productMapper.toResponse(product);
    }

    @Override
    public ProductResponse update(Long id, CreateProductRequest request) {
        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found."));

        CategoryEntity category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found."));

        productMapper.updateEntity(request, product);

        product.setCategory(category);

        productRepository.save(product);

        InventoryEntity inventory = InventoryEntity.builder()
                .product(product)
                .quantity(request.getQuantity())
                .build();

        product.setInventory(inventory);

        productRepository.save(product);

        return productMapper.toResponse(product);
    }

    @Override
    public void delete(Long id) {

        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found."));

        productRepository.delete(product);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResponse getById(Long id) {

        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found."));

        return productMapper.toResponse(product);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> getAll(Pageable pageable) {

        return productRepository.findAll(pageable)
                .map(productMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductResponse> search(ProductFilterRequest request,
                                        Pageable pageable) {

        return productRepository
                .findAll(ProductSpecification.filter(request), pageable)
                .map(productMapper::toResponse);
    }

    @Override
    @Transactional
    public ProductResponse uploadImages(Long productId, MultipartFile[] files) {

        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found."));

        for (MultipartFile file : files) {

            String imageUrl = imageService.upload(file);

            ProductImageEntity image = ProductImageEntity.builder()
                    .imageUrl(imageUrl)
                    .primary(product.getImages().isEmpty()) // First image becomes primary
                    .product(product)
                    .build();

            product.getImages().add(image);
        }

        productRepository.save(product);

        return productMapper.toResponse(product);
    }

    @Override
    @Transactional
    public void deleteImage(Long imageId) {

        ProductImageEntity image = productImageRepo.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Image not found."));

        imageService.delete(image.getImageUrl());

        productImageRepo.delete(image);
    }

    @Override
    @Transactional
    public void setPrimaryImage(Long imageId) {

        ProductImageEntity image = productImageRepo.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Image not found."));

        ProductEntity product = image.getProduct();

        product.getImages()
                .forEach(img -> img.setPrimary(false));

        image.setPrimary(true);

        productRepository.save(product);
    }
}
