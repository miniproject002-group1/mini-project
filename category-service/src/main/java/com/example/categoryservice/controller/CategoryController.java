package com.example.categoryservice.controller;

import com.example.categoryservice.base.BaseController;
import com.example.categoryservice.dto.request.CategoryRequest;
import com.example.categoryservice.dto.response.ApiResponse;
import com.example.categoryservice.dto.response.CategoryResponse;
import com.example.categoryservice.dto.response.PaginatedResponse;
import com.example.categoryservice.property.CategoryProp;
import com.example.categoryservice.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
@Tag(name = "category-controller")
@SecurityRequirement(name = "bearerAuth")
public class CategoryController extends BaseController {

    private final CategoryService categoryService;

    @PostMapping
    @Operation(summary = "Create a new category")
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(
            @RequestBody @Valid CategoryRequest categoryRequest
    ) {
        System.out.println("Category created");
        return createResponse("Category has been created successfully.",
                HttpStatus.CREATED,
                categoryService.createCategory(categoryRequest));
    }

    @GetMapping
    @Operation(summary = "Get all categories (paginated)")
    public ResponseEntity<ApiResponse<PaginatedResponse<CategoryResponse>>> getAllCategories(
            @RequestParam(defaultValue = "1") @Positive int page,
            @RequestParam(defaultValue = "10") @Positive int size,
            CategoryProp categoryProp,
            Sort.Direction direction
    ) {
        return response("Categories retrieved successfully.", categoryService.getAllCategories(page, size, categoryProp, direction));
    }

    @GetMapping("/{category-Id}")
    @Operation(summary = "Get category by ID")
    public ResponseEntity<ApiResponse<CategoryResponse>> getCategoryById(
            @PathVariable("category-Id") @Positive UUID categoryId
    ) {
        return response("Category id : " + categoryId + " retrieved successfully.",
                categoryService.getCategoryById(categoryId));
    }

    @DeleteMapping("/{category-Id}")
    @Operation(summary = "Delete category by ID")
    public ResponseEntity<ApiResponse<Object>> deleteCategoryById(
            @PathVariable("category-Id") @Positive UUID categoryId
    ) {
        categoryService.deleteCategoryById(categoryId);
        return responseForDelete("Category id : " + categoryId + " deleted successfully.");
    }

    @PutMapping("/{category-id}")
    @Operation(summary = "Update category by ID")
    public ResponseEntity<ApiResponse<CategoryResponse>> updateCategoryById(
            @PathVariable("category-id") @Positive UUID categoryId,
            @RequestBody @Valid CategoryRequest categoryRequest) {
        return response("Category id : " + categoryId + " updated successfully.",
                categoryService.updateCategoryById(categoryId,categoryRequest));
    }

}
