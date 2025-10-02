package com.example.productservice.service.Impl;


import com.example.productservice.client.CategoryClient;
import com.example.productservice.client.UserClient;
import com.example.productservice.enumeration.ProductProperties;
import com.example.productservice.exception.BadRequestException;
import com.example.productservice.exception.NotFoundException;
import com.example.productservice.exception.ServiceUnavailableException;
import com.example.productservice.model.Product;
import com.example.productservice.model.request.ProductRequest;

import com.example.productservice.model.response.*;
import com.example.productservice.repository.ProductRepository;
import com.example.productservice.service.ProductService;
import feign.FeignException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.transaction.Transactional;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final CategoryClient categoryClient;
    private final UserClient userClient;
    private final ProductRepository productRepository;
    private final CategoryIntegrationService categoryIntegrationService;
    private final UserIntegrationService userIntegrationService;

    private Product getProductByProductId(UUID productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found"));
    }

    @Override
    public PaginatedResponse<ProductResponse> getProducts(
            int page, int size, ProductProperties sortBy, Sort.Direction sortDirection) {

        Pageable pageable = PageRequest.of(page-1,size,Sort.by(sortDirection,sortBy.name()));

        Page<Product> productPage = productRepository.findAll(pageable);

        return PaginatedResponse.<ProductResponse>builder()
                .items(productPage.getContent().stream()
                        .map(product -> {
                            ApiResponse<CategoryResponse> categoryResponse =
                                    categoryClient.getCategoryById(product.getCategoryId());

                            AppUserResponse appUserResponse =
                                    userIntegrationService.getUserByUserId(product.getUserId().toString());

                            // unwrap the payload
                            return product.toProductResponse(
                                    categoryResponse.getData(),
                                    appUserResponse
                            );
                        })
                        .collect(Collectors.toList()))
                .pagination(new Pagination(productPage))
                .build();
    }


    @Override
    public ProductResponse getProductById(UUID id,String userId) {
        Product product = getProductByProductId(id);
//        CategoryResponse categoryResponse = categoryIntegrationService.getCategoryByCategoryId(product.getCategoryId());
        ApiResponse<CategoryResponse> categoryResponse = categoryClient.getCategoryById(product.getCategoryId());
        AppUserResponse appUserResponse = userIntegrationService.getUserByUserId(userId);
        return product.toProductResponse(categoryResponse.getData(), appUserResponse);
    }

//    @Override
//    @CircuitBreaker(
//            name = "categoryService",
//            fallbackMethod = "addProductFallback"
//    )
//    public ProductResponse addProduct(ProductRequest request,String userId) {
//
//        ApiResponse<CategoryResponse> categoryResponse = categoryClient.getCategoryById(request.getCategoryId());
//
//        if(categoryResponse== null || categoryResponse.getData() == null) {
//            throw new NotFoundException("Category not found");
//        }
//
//        AppUserResponse appUserResponse = userClient.getCurrentUser(userId);
//
//        if(appUserResponse== null || appUserResponse.getUserId() == null) {
//            throw new NotFoundException("User not found");
//        }
//
//        productRepository.save(request.toProduct(UUID.fromString(userId)));
//        return request.toProductResponse(categoryResponse.getData(), appUserResponse);
//    }

    public ProductResponse addProduct(ProductRequest request, String userId) {

        CategoryResponse category;
        try {
            ApiResponse<CategoryResponse> categoryResponse = categoryClient.getCategoryById(request.getCategoryId());
            category = categoryResponse.getData();
        } catch (FeignException.NotFound e) {
            throw new NotFoundException("Category not found");
        }

        AppUserResponse appUser;
        try {
            appUser = userClient.getCurrentUser(userId);
        } catch (FeignException.NotFound e) {
            throw new NotFoundException("User not found");
        }

        productRepository.save(request.toProduct(UUID.fromString(userId)));
        return request.toProductResponse(category, appUser);
    }


    @Override
    public void deleteProductById(UUID id) {
        productRepository.delete(getProductByProductId(id));
    }

    @Override
    public Product updateProductById(UUID id, ProductRequest request,String userId) {
        getProductByProductId(id);
//        categoryIntegrationService.getCategoryByCategoryId(request.getCategoryId());
        categoryClient.getCategoryById(request.getCategoryId());
        return productRepository.save(request.updateProduct(id, UUID.fromString(userId)));
    }

    @Override
    @Transactional
    public void deleteAllByCategoryId(UUID categoryId) {
        productRepository.deleteAllByCategoryId(categoryId);
    }

//    private ProductResponse addProductFallback(ProductRequest request, String userId, Throwable throwable) {
////        log.error("Fallback triggered for addProduct due to: {}", throwable.getMessage(), throwable);
//
//        throw new ServiceUnavailableException("Category service unavailable, cannot add product " , throwable);
//    }

}
