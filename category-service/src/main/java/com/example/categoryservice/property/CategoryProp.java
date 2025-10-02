package com.example.categoryservice.property;

import lombok.Getter;

@Getter
public enum CategoryProp {

    CATEGORY_ID("categoryId"),
    CATEGORY_NAME("categoryName");

    private final String field;

    CategoryProp(String field) {
        this.field = field;
    }

}
