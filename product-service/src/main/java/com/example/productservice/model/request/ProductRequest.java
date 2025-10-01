package com.example.productservice.model.request;

import com.example.productservice.model.Product;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

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

    public Product toProduct() {
        return Product.builder()
                .name(name)
                .price(price)
                .quantity(quantity)
                .build();
    }

    public Product updateProduct(Long id) {
        return Product.builder()
                .id(id)
                .name(name)
                .price(price)
                .quantity(quantity)
                .build();
    }
}
