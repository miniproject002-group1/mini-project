package com.example.categoryservice.service;


import com.example.categoryservice.dto.request.CategoryRequest;
import com.example.categoryservice.dto.response.CategoryResponse;
import com.example.categoryservice.dto.response.PaginatedResponse;
import com.example.categoryservice.property.CategoryProp;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.data.domain.Sort;

import java.util.UUID;

public interface CategoryService {
    CategoryResponse createCategory(@Valid CategoryRequest categoryRequest);

    PaginatedResponse<CategoryResponse> getAllCategories(@Positive int page, @Positive int size, CategoryProp categoryProp, Sort.Direction direction);

    CategoryResponse getCategoryById(@Positive UUID categoryId);

    void deleteCategoryById(UUID categoryId);

    CategoryResponse updateCategoryById(UUID categoryId, @Valid CategoryRequest categoryRequest);
}
