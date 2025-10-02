package com.example.categoryservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "product-service")
public interface ProductClient {

    @DeleteMapping("/products/category/{categoryId}")
    void deleteProductsByCategory(@PathVariable UUID categoryId);

}
