package com.example.categoryservice.service.serviceImp;

import com.example.categoryservice.client.UserClient;
import com.example.categoryservice.dto.request.CategoryRequest;
import com.example.categoryservice.dto.response.*;
import com.example.categoryservice.entity.Category;
import com.example.categoryservice.exception.BadRequestException;
import com.example.categoryservice.exception.NotFoundException;
import com.example.categoryservice.exception.ServiceUnavailableException;
import com.example.categoryservice.property.CategoryProp;
import com.example.categoryservice.repository.CategoryRepository;
import com.example.categoryservice.service.CategoryService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImp implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserClient userClient;

    @Override
    @CircuitBreaker(name = "userCB",fallbackMethod = "createCategoryFallback")
    public CategoryResponse createCategory(CategoryRequest categoryRequest) {
        AppUserResponse userResponse = userClient.getCurrentUserProfile().getData();
        UUID userId = userResponse != null ? userResponse.getUserId() : null;
        if (userId == null) {
            throw new BadRequestException("Cannot create category: user-service unavailable or userId missing");
        }
        Category category = categoryRequest.toEntity();
        category.setUserId(userResponse.getUserId());
        return categoryRepository.save(category).toResponse(userResponse);
    }

    @Override
    @CircuitBreaker(name = "userCB",fallbackMethod = "getAllCategoriesFallback")
    public PaginatedResponse<CategoryResponse> getAllCategories(int page, int size, CategoryProp categoryProp, Sort.Direction direction) {
        AppUserResponse userResponse = userClient.getCurrentUserProfile().getData();
        UUID userId = userResponse != null ? userResponse.getUserId() : null;

        if (userId == null) {
            throw new IllegalStateException("Cannot fetch categories: user-service unavailable or userId missing");
        }

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(direction, categoryProp.getField()));
        Page<Category> categoryPage = categoryRepository.findAllByUserId(userId, pageable);

        return PaginatedResponse.<CategoryResponse>builder()
                .items(categoryPage.getContent().stream()
                        .map(category -> category.toResponse(userResponse))
                        .collect(Collectors.toList()))
                .pagination(new Pagination(categoryPage))
                .build();
    }

    @Override
    @CircuitBreaker(name = "userCB",fallbackMethod = "getCategoryByIdFallback")
    public CategoryResponse getCategoryById(UUID categoryId) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new NotFoundException("Category " + categoryId + " Not Found"));
        AppUserResponse userResponse = userClient.getCurrentUserProfile().getData();
        UUID userId = userResponse != null ? userResponse.getUserId() : null;
        if (userId == null) {
            throw new BadRequestException("Cannot create category: user-service unavailable or userId missing");
        }
        return category.toResponse(userResponse);
    }

    @Override
    public void deleteCategoryById(UUID categoryId) {
        categoryRepository.findById(categoryId).orElseThrow(() -> new NotFoundException("Category " + categoryId + " Not Found"));
        categoryRepository.deleteById(categoryId);
    }

    @Override
    @CircuitBreaker(name = "userCB",fallbackMethod = "updateCategoryFallback")
    public CategoryResponse updateCategoryById(UUID categoryId, CategoryRequest categoryRequest) {
        AppUserResponse userResponse = userClient.getCurrentUserProfile().getData();
        System.out.println("userResponse: " + userResponse);
        UUID userId = userResponse != null ? userResponse.getUserId() : null;
        if (userId == null) {
            throw new BadRequestException("Cannot update category: user-service unavailable or userId missing");
        }
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new NotFoundException("Category " + categoryId + " Not Found"));
        category.setCategoryName(categoryRequest.getName());
        category.setCategoryDescription(categoryRequest.getDescription());
        return categoryRepository.save(category).toResponse(userResponse);
    }

    public CategoryResponse createCategoryFallback(CategoryRequest request, Throwable throwable) {
        throw new ServiceUnavailableException("Cannot create category: user-service unavailable");
    }

    public CategoryResponse updateCategoryFallback(UUID categoryId, CategoryRequest categoryRequest, Throwable throwable) {
        // Option 1: fail if user-service is down
        throw new ServiceUnavailableException("Cannot update category: user-service unavailable or userId missing");

        // Option 2: if you want to allow update but userResponse = null:
        // Category category = categoryRepository.findById(categoryId)
        //         .orElseThrow(() -> new NotFoundException("Category " + categoryId + " Not Found"));
        // return category.toResponse(null);
    }

    public CategoryResponse getCategoryByIdFallback(UUID categoryId, Throwable throwable) {
        throw new ServiceUnavailableException("Cannot find category by id : user-service unavailable");
//        Category category = categoryRepository.findById(categoryId)
//                .orElseThrow(() -> new NotFoundException("Category " + categoryId + " Not Found"));
//        return category.toResponse(null);
    }

    public PaginatedResponse<CategoryResponse> getAllCategoriesFallback(int page, int size, CategoryProp categoryProp, Sort.Direction direction, Throwable throwable) {
        throw new ServiceUnavailableException("Cannot get all category: user-service unavailable");
    }

}
