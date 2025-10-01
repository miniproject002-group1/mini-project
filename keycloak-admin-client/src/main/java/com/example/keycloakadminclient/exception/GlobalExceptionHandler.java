package com.example.keycloakadminclient.exception;

import com.example.keycloakadminclient.dto.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  // Handles our custom "User already exists" exception
  @ExceptionHandler(UserAlreadyExistsException.class)
  public ResponseEntity<ErrorResponse> handleUserAlreadyExists(UserAlreadyExistsException ex, HttpServletRequest request) {
    log.warn("Conflict: {}", ex.getMessage());
    return buildErrorResponse(ex, HttpStatus.CONFLICT, request);
  }

  // Handles our custom "Invalid credentials" exception
  @ExceptionHandler(InvalidCredentialsException.class)
  public ResponseEntity<ErrorResponse> handleInvalidCredentials(InvalidCredentialsException ex, HttpServletRequest request) {
    log.warn("Unauthorized: {}", ex.getMessage());
    return buildErrorResponse(ex, HttpStatus.UNAUTHORIZED, request);
  }

  // Handles exceptions from the Keycloak Admin Client when a user is not found
  @ExceptionHandler(jakarta.ws.rs.NotFoundException.class)
  public ResponseEntity<ErrorResponse> handleUserNotFound(jakarta.ws.rs.NotFoundException ex, HttpServletRequest request) {
    log.warn("User not found: {}", ex.getMessage());
    ErrorResponse errorResponse = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.NOT_FOUND.value())
            .error("Not Found")
            .message("The requested user could not be found.")
            .path(request.getRequestURI())
            .build();
    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
  }

  // Handles validation errors from @Valid
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
    String errors = ex.getBindingResult().getFieldErrors().stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .collect(Collectors.joining(", "));
    log.warn("Validation error: {}", errors);
    ErrorResponse errorResponse = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.BAD_REQUEST.value())
            .error("Validation Failed")
            .message(errors)
            .path(request.getRequestURI())
            .build();
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  // A general handler for all other exceptions
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex, HttpServletRequest request) {
    log.error("An unexpected error occurred", ex);
    return buildErrorResponse(ex, HttpStatus.INTERNAL_SERVER_ERROR, request);
  }

  // Helper method to build a consistent error response
  private ResponseEntity<ErrorResponse> buildErrorResponse(Exception ex, HttpStatus status, HttpServletRequest request) {
    ErrorResponse errorResponse = ErrorResponse.builder()
            .timestamp(LocalDateTime.now())
            .status(status.value())
            .error(status.getReasonPhrase())
            .message(ex.getMessage())
            .path(request.getRequestURI())
            .build();
    return new ResponseEntity<>(errorResponse, status);
  }
}