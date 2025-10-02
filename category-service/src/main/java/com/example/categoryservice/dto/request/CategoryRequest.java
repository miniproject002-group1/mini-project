package com.example.categoryservice.dto.request;

import com.example.categoryservice.entity.Category;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CategoryRequest {

    private String name;
    private String description;

    public Category toEntity() {
        return Category.builder()
                .categoryName(name)
                .categoryDescription(description)
                .build();
    }

}
