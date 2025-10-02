package com.example.productservice.base;


import com.example.productservice.model.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

public class BaseController {

    protected ResponseEntity<ApiResponse<Object>> responseForDelete(String message) {
        return ResponseEntity.ok(ApiResponse.builder()
                .success(true)
                .message(message)
                .instant(LocalDateTime.now())
                .build());
    }

    protected <T> ResponseEntity<com.example.productservice.model.response.ApiResponse<T>> response(String message, T payload) {
        ApiResponse<T> apiResponse = ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .status(HttpStatus.OK)
                .data(payload)
                .instant(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    protected <T> ResponseEntity<ApiResponse<T>> createResponse(String message, HttpStatus httpStatus, T payload) {
        ApiResponse<T> apiResponse = ApiResponse.<T>builder()
                .success(true)
                .message(message)
                .status(httpStatus)
                .data(payload)
                .instant(LocalDateTime.now())
                .build();
        return ResponseEntity.status(httpStatus).body(apiResponse);
    }

}
