package com.example.productservice.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Double price;

    private Integer quantity;

//    private Category category;
//
//    private User userId
}
