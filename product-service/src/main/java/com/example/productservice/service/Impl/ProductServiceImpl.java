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
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final CategoryClient categoryClient;
    private final UserClient userClient;
    private final ProductRepository productRepository;

    private Product getProductByProductId(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found"));
    }

    @Override
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    @Override
    public ProductResponse getProductById(UUID id) {
        Product product =getProductByProductId(id);
        CategoryResponse categoryResponse = categoryClient.getCategoryById(product.getCategoryId());
        UserResponse userResponse = userClient.getUserById(product.getUserId());
        return product.toProductResponse(categoryResponse, userResponse);
    }

    @Override
    public ProductResponse addProduct(ProductRequest request) {
        CategoryResponse categoryResponse = categoryClient.getCategoryById(request.getCategoryId());
        if(categoryResponse.getCategoryId() == null){
            throw new NotFoundException("Category Id not found");
        }
        UUID userId = UUID.randomUUID();//mock user id sin
        UserResponse userResponse = userClient.getUserById(userId);
        if(userResponse.getUserId() == null){
            throw new NotFoundException("User Id not found");
        }
        productRepository.save(request.toProduct());
        return request.toProductResponse(categoryResponse,userResponse);
    }

    @Override
    public APIResponse<Void> deleteProductById(UUID id) {
        productRepository.delete(getProductByProductId(id));
        return new APIResponse<>(
                "Deleted product successfully",
                null,
                HttpStatus.OK,
                LocalDateTime.now()
        );
    }

    @Override
    public Product updateProductById(UUID id, ProductRequest request) {
        getProductByProductId(id);
        return productRepository.save(request.updateProduct(id));
    }
}
