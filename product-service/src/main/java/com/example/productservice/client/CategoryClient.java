package com.example.productservice.client;

import com.example.productservice.model.response.CategoryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "category-service")
public interface CategoryClient {
    @GetMapping("/{id}")
    CategoryResponse getCategoryById(Long id);
}
