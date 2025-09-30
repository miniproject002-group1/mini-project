package com.example.productservice.service.Impl;

import com.example.productservice.model.Product;
import com.example.productservice.model.request.ProductRequest;
import com.example.productservice.repository.ProductRepository;
import com.example.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    @Override
    public Product addProduct(ProductRequest request) {
        return productRepository.save(request.toProduct());
    }

    @Override
    public void delete(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        productRepository.delete(product);
    }

    @Override
    public Product update(Long id, ProductRequest request) {
        productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return productRepository.save(request.updateProduct(id));
    }
}
