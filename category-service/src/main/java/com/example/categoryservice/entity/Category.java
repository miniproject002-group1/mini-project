package com.example.categoryservice.entity;

import com.example.categoryservice.dto.response.CategoryResponse;
import com.example.categoryservice.dto.response.AppUserResponse;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "categories")
@Entity
@ToString
@Builder
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "category_id", updatable = false, nullable = false)
    private UUID categoryId;

    private String categoryName;
    private String categoryDescription;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    public CategoryResponse toResponse(AppUserResponse userResponse) {
        return CategoryResponse.builder()
                .categoryId(categoryId)
                .categoryName(categoryName)
                .categoryDescription(categoryDescription)
                .userResponse(userResponse)
                .build();
    }

}
