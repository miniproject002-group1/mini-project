package com.example.productservice.controller;

import com.example.productservice.enumeration.ProductProperties;
import com.example.productservice.model.Product;
import com.example.productservice.model.request.ProductRequest;
import com.example.productservice.model.response.APIResponse;
import com.example.productservice.model.response.ProductResponse;
import com.example.productservice.model.response.UserResponse;
import com.example.productservice.service.ProductService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public APIResponse<List<ProductResponse>> getProducts(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam ProductProperties sortBy,
            @RequestParam Sort.Direction sortDirection
            ) {
        return new APIResponse<>(
                "Get all products successfully",
                productService.getProducts(page,size,sortBy,sortDirection),
                HttpStatus.OK,
                LocalDateTime.now()
        );
    }

    @GetMapping("/{product-id}")
    public APIResponse<ProductResponse> getProductById(@PathVariable("product-id") UUID id, @AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        return new APIResponse<>(
                "Get product by id successfully",
                productService.getProductById(id,userId),
                HttpStatus.OK,
                LocalDateTime.now()
        );
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public APIResponse<ProductResponse> addProduct(@Valid @RequestBody ProductRequest product,@AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        return new APIResponse<>(
                "Inserted new product successfully",
                productService.addProduct(product,userId),
                HttpStatus.CREATED,
                LocalDateTime.now()
        );
    }

    @DeleteMapping("/{product-id}")
    public APIResponse<Void> deleteProductById(@PathVariable("product-id") UUID id) {
        return productService.deleteProductById(id);
    }

    @PutMapping("/{product-id}")
    public APIResponse<Product> updateProductById(@PathVariable("product-id") UUID id,
                                                  @Valid @RequestBody ProductRequest product,
                                                  @AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        return new APIResponse<>(
                "Updated product successfully",
                productService.updateProductById(id,product,userId),
                HttpStatus.OK,
                LocalDateTime.now()
        );
    }
}
