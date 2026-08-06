package com.ecommerce.category.service.impl;

import com.ecommerce.category.dto.CategoryResponse;
import com.ecommerce.category.dto.CreateCategoryRequest;
import com.ecommerce.category.entity.CategoryEntity;
import com.ecommerce.category.repository.CategoryRepo;
import com.ecommerce.category.service.CategoryService;
import com.ecommerce.common.exception.ResourceAlreadyExistsException;
import com.ecommerce.common.exception.ResourceNotFoundException;
import com.ecommerce.mapper.CategoryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;
    private final CategoryRepo categoryRepository;

    @Override
    public CategoryResponse create(CreateCategoryRequest request) {

        if (categoryRepository.existsByNameIgnoreCase(request.getName().trim())) {
            throw new ResourceAlreadyExistsException("Category already exists.");
        }

        CategoryEntity category = categoryMapper.toEntity(request);

        categoryRepository.save(category);

        return categoryMapper.toResponse(category);
    }

    @Override
    public CategoryResponse update(Long id, CreateCategoryRequest request) {

        CategoryEntity category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found."));

        if (!category.getName().equalsIgnoreCase(request.getName().trim())
                && categoryRepository.existsByNameIgnoreCase(request.getName().trim())) {
            throw new ResourceAlreadyExistsException("Category already exists.");
        }

        categoryMapper.updateEntity(request, category);

        categoryRepository.save(category);

        return categoryMapper.toResponse(category);
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

        return categoryMapper.toResponse(category);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponse> getAll() {

        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toResponse)
                .toList();
    }
}
