package com.ecommerce.category.service.impl;

import com.ecommerce.category.dto.CategoryResponse;
import com.ecommerce.category.dto.CreateCategoryRequest;
import com.ecommerce.category.entity.CategoryEntity;
import com.ecommerce.category.service.CategoryService;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Override
    public CategoryResponse create(CreateCategoryRequest request) {

        if (categoryRepository.existsByNameIgnoreCase(request.getName())) {
            throw new ResourceAlreadyExistsException("Category already exists.");
        }

        CategoryEntity category = CategoryEntity.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();

        categoryRepository.save(category);

        return mapToResponse(category);
    }

    @Override
    public CategoryResponse update(Long id, UpdateCategoryRequest request) {

        CategoryEntity category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found."));

        if (!category.getName().equalsIgnoreCase(request.getName())
                && categoryRepository.existsByNameIgnoreCase(request.getName())) {
            throw new ResourceAlreadyExistsException("Category already exists.");
        }

        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setActive(request.getActive());

        categoryRepository.save(category);

        return mapToResponse(category);
    }

    @Override
    public void delete(Long id) {

        CategoryEntity category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found."));

        categoryRepository.delete(category);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse getById(Long id) {

        CategoryEntity category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found."));

        return mapToResponse(category);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> getAll() {

        return categoryRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }
}
