package com.example.productservice.service.Impl;

import com.example.productservice.client.CategoryClient;
import com.example.productservice.client.UserClient;
import com.example.productservice.enumeration.ProductProperties;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final CategoryClient categoryClient;
    private final ProductRepository productRepository;
    private final CategoryIntegrationService categoryIntegrationService;
    private final UserIntegrationService userIntegrationService;

    private Product getProductByProductId(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found"));
    }

    @Override
    public List<ProductResponse> getProducts(
            int page, int size, ProductProperties sortBy, Sort.Direction sortDirection) {

        Pageable pageable = PageRequest.of(page-1,size,Sort.by(sortDirection,sortBy.name()));

        Page<Product> productResponses = productRepository.findAll(pageable);

        return productResponses.getContent().stream().map(
                product -> {
                    CategoryResponse categoryResponse = categoryClient.getCategoryById(product.getCategoryId());
                    UserResponse userResponse = userIntegrationService.getUserByUserId(product.getUserId().toString());
                    return product.toProductResponse(categoryResponse,userResponse);
                }).toList();
    }


    @Override
    public ProductResponse getProductById(UUID id,String userId) {
        Product product = getProductByProductId(id);
        CategoryResponse categoryResponse = categoryIntegrationService.getCategoryByCategoryId(product.getCategoryId());
        UserResponse userResponse = userIntegrationService.getUserByUserId(userId);
        return product.toProductResponse(categoryResponse, userResponse);
    }

    @Override
    public ProductResponse addProduct(ProductRequest request,String userId) {

        CategoryResponse categoryResponse = categoryIntegrationService.getCategoryByCategoryId(request.getCategoryId());

        UserResponse userResponse = userIntegrationService.getUserByUserId(userId);
        if(userResponse == null || userResponse.getId() == null){
            throw new NotFoundException("Invalid User");
        }

        productRepository.save(request.toProduct(UUID.fromString(userId)));
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
    public Product updateProductById(UUID id, ProductRequest request,String userId) {
        getProductByProductId(id);
        categoryIntegrationService.getCategoryByCategoryId(request.getCategoryId());
        return productRepository.save(request.updateProduct(id, UUID.fromString(userId)));
    }

}
