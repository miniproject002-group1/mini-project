package com.example.productservice.model.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponse {
    private UUID categoryId;
    private String categoryName;
    private String categoryDescription;
    @JsonIgnore
    private AppUserResponse userResponse;
}
