package com.example.categoryservice.dto.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class ApiResponse<T> {

    private boolean success;
    private String message;
    private HttpStatus status;
    private T data;
    private LocalDateTime instant;

}
