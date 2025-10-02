package com.example.productservice.service.Impl;

import com.example.productservice.client.CategoryClient;
import com.example.productservice.exception.BadRequestException;
import com.example.productservice.exception.NotFoundException;
import com.example.productservice.exception.ServiceUnavailableException;
import com.example.productservice.model.response.CategoryResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryIntegrationService {

    private final CategoryClient categoryClient;

    @CircuitBreaker(name = "categoryService", fallbackMethod = "categoryFallback")
    public CategoryResponse getCategoryByCategoryId(UUID categoryId) {
        CategoryResponse categoryResponse = categoryClient.getCategoryById(categoryId);
        if(categoryResponse == null ||categoryResponse.getCategoryId() == null) {
            throw new NotFoundException("Category Id not found");
        }
        return categoryResponse;
    }

    public CategoryResponse categoryFallback(Throwable ex) {
        throw new ServiceUnavailableException(
                "Category Service unavailable : " + ex.getMessage());
    }

}
