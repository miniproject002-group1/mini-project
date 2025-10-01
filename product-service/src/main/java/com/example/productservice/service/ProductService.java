package com.example.productservice.service;

import com.example.productservice.model.Product;
import com.example.productservice.model.request.ProductRequest;
import com.example.productservice.model.response.APIResponse;
import com.example.productservice.model.response.ProductResponse;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductService {
    List<Product> getProducts();

    ProductResponse getProductById(Long id);

    Product addProduct(ProductRequest product);

    APIResponse<Void> deleteProductById(Long id);

    Product updateProductById(Long id, ProductRequest product);
}
