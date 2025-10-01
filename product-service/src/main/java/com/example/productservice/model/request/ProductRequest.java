package com.example.productservice.model.request;

import com.example.productservice.model.Product;
import com.example.productservice.model.response.CategoryResponse;
import com.example.productservice.model.response.ProductResponse;
import com.example.productservice.model.response.UserResponse;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {

    @NotBlank(message = "Product name is required")
    private String name;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than 0")
    private Double price;

    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity cannot be negative")
    private Integer quantity;

    @NotNull(message = "Category Id is required")
    private UUID categoryId;

    public Product toProduct() {
        return Product.builder()
                .name(name)
                .price(price)
                .quantity(quantity)
                .categoryId(categoryId)
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

    public Product updateProduct(UUID id) {
        return Product.builder()
                .id(id)
                .name(name)
                .price(price)
                .quantity(quantity)
                .build();
    }
}
