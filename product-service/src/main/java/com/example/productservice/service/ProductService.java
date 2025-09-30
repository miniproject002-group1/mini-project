package com.example.productservice.service;

import com.example.productservice.model.Product;
import com.example.productservice.model.request.ProductRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ProductService {
    List<Product> findAll();

    Product findById(Long id);

    Product addProduct(ProductRequest product);

    void delete(Long id);

    Product update(Long id, ProductRequest product);
}
