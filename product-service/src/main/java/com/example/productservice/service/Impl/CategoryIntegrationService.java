package com.example.productservice.service.Impl;

import com.example.productservice.client.CategoryClient;
import com.example.productservice.exception.BadRequestException;
import com.example.productservice.exception.NotFoundException;
//import com.example.productservice.exception.ServiceUnavailableException;
import com.example.productservice.exception.ServiceUnavailableException;
import com.example.productservice.model.response.ApiResponse;
import com.example.productservice.model.response.CategoryResponse;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryIntegrationService {

    private final CategoryClient categoryClient;

//    @CircuitBreaker(name = "categoryService", fallbackMethod = "categoryFallback")
////    public CategoryResponse getCategoryByCategoryId(UUID categoryId) {
////        ApiResponse<CategoryResponse> response = categoryClient.getCategoryById(categoryId);
////
////        if (response == null || response.getData() == null) {
////            throw new NotFoundException("Category Id not found");
////        }
////
////        return response.getData();
////    }
//
//    public CategoryResponse getCategoryByCategoryId(UUID categoryId) {
//        try {
//            ApiResponse<CategoryResponse> response = categoryClient.getCategoryById(categoryId);
//
//            if (response == null || response.getData() == null) {
//                throw new NotFoundException("Category Id not found");
//            }
//
//            return response.getData();
//
//        } catch (FeignException.NotFound e) {
//            // This is the key: throw 404 for Feign 404
//            throw new NotFoundException("Category Id not found: " + categoryId);
//        } catch (FeignException e) {
//            // Other Feign errors can be treated as service unavailable
//            throw new ServiceUnavailableException("Category service unavailable: " + e.getMessage());
//        }
//    }



//public CategoryResponse getCategoryByCategoryId(UUID categoryId) {
//    ApiResponse<CategoryResponse> response = categoryClient.getCategoryById(categoryId);
//
//    if (response == null || response.getData() == null) {
//        throw new NotFoundException("Category Id not found: " + categoryId);
//    }
//
//    return response.getData();
//}







//    public CategoryResponse categoryFallback(Throwable ex) {
//        throw new ServiceUnavailableException(
//                "Category Service : " + ex.getMessage());
//    }

}
