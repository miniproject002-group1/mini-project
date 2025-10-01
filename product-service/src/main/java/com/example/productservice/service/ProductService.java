package com.example.productservice.service;

import com.example.productservice.model.Product;
import com.example.productservice.model.request.ProductRequest;
import com.example.productservice.model.response.APIResponse;
import com.example.productservice.model.response.ProductResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    List<Product> getProducts();

    ProductResponse getProductById(UUID id);

    ProductResponse addProduct(ProductRequest product);

    APIResponse<Void> deleteProductById(UUID id);

    Product updateProductById(UUID id, ProductRequest product);
}
