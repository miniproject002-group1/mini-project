package com.example.categoryservice.dto.response;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryResponse {

    private UUID categoryId;
    private String categoryName;
    private String categoryDescription;
    private AppUserResponse userResponse;
}
