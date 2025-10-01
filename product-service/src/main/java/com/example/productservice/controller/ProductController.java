package com.example.productservice.controller;

import com.example.productservice.model.Product;
import com.example.productservice.model.request.ProductRequest;
import com.example.productservice.model.response.APIResponse;
import com.example.productservice.model.response.ProductResponse;
import com.example.productservice.service.ProductService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public APIResponse<List<Product>> getProducts() {
        return new APIResponse<>(
                "Get all products successfully",
                productService.getProducts(),
                HttpStatus.OK,
                LocalDateTime.now()
        );
    }

    @GetMapping("/{product-id}")
    public APIResponse<ProductResponse> getProductById(@PathVariable("product-id") Long id) {
        return new APIResponse<>(
                "Get product by id successfully",
                productService.getProductById(id),
                HttpStatus.OK,
                LocalDateTime.now()
        );
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public APIResponse<Product> addProduct(@Valid @RequestBody ProductRequest product) {
        return new APIResponse<>(
                "Inserted new product successfully",
                productService.addProduct(product),
                HttpStatus.CREATED,
                LocalDateTime.now()
        );
    }

    @DeleteMapping("/{product-id}")
    public APIResponse<Void> deleteProductById(@PathVariable("product-id") Long id) {
        return productService.deleteProductById(id);
    }

    @PutMapping("/{product-id}")
    public APIResponse<Product> updateProductById(@PathVariable("product-id") Long id, @Valid @RequestBody ProductRequest product) {
        return new APIResponse<>(
                "Updated product successfully",
                productService.updateProductById(id,product),
                HttpStatus.OK,
                LocalDateTime.now()
        );
    }
}
