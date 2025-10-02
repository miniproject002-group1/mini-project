package com.example.categoryservice.dto.request;

import com.example.categoryservice.entity.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CategoryRequest {

    @NotBlank
    private String name;

    private String description;

    public Category toEntity() {
        return Category.builder()
                .categoryName(name)
                .categoryDescription(description)
                .build();
    }

}
