package com.example.productservice.controller;



import com.example.productservice.base.BaseController;
import com.example.productservice.enumeration.ProductProperties;
import com.example.productservice.model.Product;
import com.example.productservice.model.request.ProductRequest;
import com.example.productservice.model.response.ApiResponse;
import com.example.productservice.model.response.PaginatedResponse;
import com.example.productservice.model.response.ProductResponse;
import com.example.productservice.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
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
public class ProductController extends BaseController {
    private final ProductService productService;

    @GetMapping
    @Operation(
            summary = "Get all products (paginated)",
            description = "Retrieves a paginated list of all products. Support pagination, sorting by product properties, and direction (ASC/DESC).")
    public ResponseEntity<ApiResponse<PaginatedResponse<ProductResponse>>> getProducts(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam ProductProperties sortBy,
            @RequestParam Sort.Direction sortDirection
            ) {
        return response(
                "Get all products successfully",
                productService.getProducts(page,size,sortBy,sortDirection)
        );
    }

    @GetMapping("/{product-id}")
    @Operation(
            summary = "Get product by ID",
            description = "Retrieves a single product by its unique identifier (UUID). Return the full product details if found.")
    public ResponseEntity<ApiResponse<ProductResponse>> getProductById(@PathVariable("product-id") UUID id, @AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        return response(
                "Get product by id successfully",
                productService.getProductById(id,userId)
        );
    }

    @PostMapping
    @Operation(
            summary = "Create a new product",
            description = "Creates a new product using the provided request payload. The request must include the product's name, price, and description.")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiResponse<ProductResponse>> addProduct(@Valid @RequestBody ProductRequest product,@AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        return createResponse(
                "Inserted new product successfully",
                HttpStatus.CREATED,
                productService.addProduct(product,userId)
        );
    }

    @DeleteMapping("/{product-id}")
    @Operation(
            summary = "Delete product by ID",
            description = "Deletes an existing product identified by its UUID. After deletion the product can no longer be retrieved.")
    public ResponseEntity<ApiResponse<Object>> deleteProductById(@PathVariable("product-id") UUID id) {
        productService.deleteProductById(id);
        return responseForDelete("Product id : " + id + " deleted successfully.");
    }

    @PutMapping("/{product-id}")
    @Operation(
            summary = "Update product by ID",
            description = "Updates an existing product using the provided request payload. The product is identified by its UUID")
    public ApiResponse<Product> updateProductById(@PathVariable("product-id") UUID id,
                                                  @Valid @RequestBody ProductRequest product,
                                                  @AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        return new ApiResponse<>(
                true,
                "Updated product successfully",
                HttpStatus.OK,
                productService.updateProductById(id,product,userId),
                LocalDateTime.now()
        );
    }

    @DeleteMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<Object>> deleteProductsByCategory(@PathVariable UUID categoryId) {
        productService.deleteAllByCategoryId(categoryId);
        return responseForDelete("categoryId id : " + categoryId + " deleted successfully.");
    }

}
