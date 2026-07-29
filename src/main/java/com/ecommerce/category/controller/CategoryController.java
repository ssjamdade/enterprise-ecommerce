package com.ecommerce.category.controller;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponse>> create(
            @Valid @RequestBody CreateCategoryRequest request) {

        return ResponseEntity.ok(
                ApiResponse.<CategoryResponse>builder()
                        .success(true)
                        .message("Category created successfully.")
                        .data(categoryService.create(request))
                        .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCategoryRequest request) {

        return ResponseEntity.ok(
                ApiResponse.<CategoryResponse>builder()
                        .success(true)
                        .message("Category updated successfully.")
                        .data(categoryService.update(id, request))
                        .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {

        categoryService.delete(id);

        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .success(true)
                        .message("Category deleted successfully.")
                        .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryResponse>> getById(@PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.<CategoryResponse>builder()
                        .success(true)
                        .message("Category fetched successfully.")
                        .data(categoryService.getById(id))
                        .build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getAll() {

        return ResponseEntity.ok(
                ApiResponse.<List<CategoryResponse>>builder()
                        .success(true)
                        .message("Categories fetched successfully.")
                        .data(categoryService.getAll())
                        .build());
    }
}