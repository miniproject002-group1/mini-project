package com.example.keycloakadminclient.dto.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public abstract class BaseResponse {

  protected <T> ResponseEntity<ApiResponse<T>> responseEntity(
          boolean success,
          String message,
          HttpStatus status,
          T data
  ) {
    ApiResponse<T> response = ApiResponse.<T>builder()
            .success(success)
            .message(message)
            .status(HttpStatus.OK)
            .data(data)
            .build();

    return new ResponseEntity<>(response, status);
  }

  protected <T> ResponseEntity<ApiResponse<T>> responseEntity(
          boolean success,
          String message,
          HttpStatus status
  ) {
    return responseEntity(success, message, status, null);
  }
}