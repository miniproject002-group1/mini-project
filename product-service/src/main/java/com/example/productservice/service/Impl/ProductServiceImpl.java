package com.example.productservice.service.Impl;

import com.example.productservice.client.CategoryClient;
import com.example.productservice.client.UserClient;
import com.example.productservice.exception.NotFoundException;
import com.example.productservice.model.Product;
import com.example.productservice.model.request.ProductRequest;
import com.example.productservice.model.response.APIResponse;
import com.example.productservice.model.response.CategoryResponse;
import com.example.productservice.model.response.ProductResponse;
import com.example.productservice.model.response.UserResponse;
import com.example.productservice.repository.ProductRepository;
import com.example.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final CategoryClient categoryClient;
    private final UserClient userClient;
    private final ProductRepository productRepository;
    @Override
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    @Override
    public ProductResponse getProductById(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));

        CategoryResponse categoryResponse = categoryClient.getCategoryById(product.getCategoryId());
        UserResponse userResponse = userClient.getUserById(product.getUserId());

        return product.toProductResponse(categoryResponse, userResponse);
    }

    @Override
    public Product addProduct(ProductRequest request) {
        return productRepository.save(request.toProduct());
    }

    @Override
    public APIResponse<Void> deleteProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));
        productRepository.delete(product);
        return new APIResponse<>(
                "Deleted product successfully",
                null,
                HttpStatus.OK,
                LocalDateTime.now()
        );
    }

    @Override
    public Product updateProductById(Long id, ProductRequest request) {
        productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));
        return productRepository.save(request.updateProduct(id));
    }
}
