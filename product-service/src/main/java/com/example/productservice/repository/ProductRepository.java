package com.example.productservice.repository;

import com.example.productservice.model.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    @Transactional
    void deleteAllByCategoryId(UUID categoryId);
}
