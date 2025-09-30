package com.example.productservice.model.request;

import com.example.productservice.model.Product;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
    private String name;
    private Double price;
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
