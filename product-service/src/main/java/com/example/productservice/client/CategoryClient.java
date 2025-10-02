package com.example.productservice.client;

import com.example.productservice.model.response.ApiResponse;
import com.example.productservice.model.response.CategoryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "category-service",path = "/api/v1/categories")
public interface CategoryClient {
    @GetMapping("/{category-Id}")
    ApiResponse<CategoryResponse> getCategoryById(@PathVariable("category-Id") UUID categoryId);
}
