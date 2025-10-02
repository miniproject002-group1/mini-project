package com.example.productservice.model.request;

import com.example.productservice.model.Product;
import com.example.productservice.model.response.CategoryResponse;
import com.example.productservice.model.response.ProductResponse;
import com.example.productservice.model.response.UserResponse;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {

    @NotBlank(message = "Product name is required")
    @Size(min = 3, max = 100, message = "Product name must be between 3 and 100 characters")
    private String name;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", inclusive = true, message = "Price must be greater than 0")
    @Digits(integer = 10, fraction = 2, message = "Price must be a valid monetary amount")
    private Double price;

    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity cannot be negative")
    @Max(value = 10000, message = "Quantity cannot exceed 10,000")
    private Integer quantity;

    @NotNull(message = "Category Id is required")
    private UUID categoryId;

    public Product toProduct(UUID userId) {
        return Product.builder()
                .name(name != null?name.trim():null)
                .price(price)
                .quantity(quantity)
                .categoryId(categoryId)
                .userId(userId)
                .build();
    }

    public ProductResponse toProductResponse(CategoryResponse categoryResponse, UserResponse userResponse) {
        return ProductResponse.builder()
                .name(name)
                .price(price)
                .quantity(quantity)
                .category(categoryResponse)
                .user(userResponse)
                .build();
    }

    public Product updateProduct(UUID id,UUID userId) {
        return Product.builder()
                .id(id)
                .name(name.trim())
                .price(price)
                .quantity(quantity)
                .categoryId(categoryId)
                .userId(userId)
                .build();
    }
}
