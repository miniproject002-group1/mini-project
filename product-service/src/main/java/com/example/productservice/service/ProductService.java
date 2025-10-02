package com.example.productservice.service;

import com.example.productservice.enumeration.ProductProperties;
import com.example.productservice.model.Product;
import com.example.productservice.model.request.ProductRequest;
import com.example.productservice.model.response.ApiResponse;
import com.example.productservice.model.response.PaginatedResponse;
import com.example.productservice.model.response.ProductResponse;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    PaginatedResponse<ProductResponse> getProducts(int page, int size, ProductProperties sortBy, Sort.Direction sortDirection);

    ProductResponse getProductById(UUID id,String userId);

    ProductResponse addProduct(ProductRequest product,String userId);

    void deleteProductById(UUID id);

    Product updateProductById(UUID id, ProductRequest product,String userId);
}
