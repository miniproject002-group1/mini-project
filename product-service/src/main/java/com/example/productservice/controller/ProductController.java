package com.example.productservice.controller;

import com.example.productservice.model.Product;
import com.example.productservice.model.request.ProductRequest;
import com.example.productservice.service.ProductService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<Product>> getProducts() {
        return ResponseEntity.ok(productService.findAll());
    }

    @GetMapping("/{product-id}")
    public ResponseEntity<Product> getProductById(@PathVariable("product-id") Long id) {
        return ResponseEntity.ok(productService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody ProductRequest product) {
        return ResponseEntity.ok(productService.addProduct(product));
    }

    @DeleteMapping("/{product-id}")
    public void deleteProductById(@PathVariable("product-id") Long id) {
        productService.delete(id);
    }

    @PutMapping("/{product-id}")
    public ResponseEntity<Product> updateProduct(@PathVariable("product-id") Long id, @RequestBody ProductRequest product) {
        return ResponseEntity.ok(productService.update(id,product));
    }
}
