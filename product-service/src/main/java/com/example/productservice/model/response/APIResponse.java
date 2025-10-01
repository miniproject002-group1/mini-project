package com.example.productservice.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class APIResponse<T> {
  private String message;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  private T payload;

  private HttpStatus status;
  private LocalDateTime instant;
}
