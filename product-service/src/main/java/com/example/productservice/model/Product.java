package com.example.productservice.model;

import com.example.productservice.model.response.CategoryResponse;
import com.example.productservice.model.response.ProductResponse;
import com.example.productservice.model.response.UserResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "products")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    private Double price;

    private Integer quantity;

    @Column(nullable = false)
    private UUID categoryId;

    @Column(nullable = false)
    private UUID userId;

    public ProductResponse toProductResponse(CategoryResponse category, UserResponse user) {
        return ProductResponse.builder()
                .productId(id)
                .name(name)
                .price(price)
                .quantity(quantity)
                .category(category)
                .user(user)
                .build();
    }
}
