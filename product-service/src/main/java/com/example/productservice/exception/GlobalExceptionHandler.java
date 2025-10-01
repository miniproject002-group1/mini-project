package com.example.productservice.exception;

import io.jsonwebtoken.MalformedJwtException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;

//import org.springframework.security.access.AccessDeniedException;
//import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.net.URI;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

  // Logger for the catch-all handler
  private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  // Helper method to create a consistent ProblemDetail base
  private ProblemDetail createProblemDetail(HttpStatus status, String detail, WebRequest request) {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, detail);
    problemDetail.setInstance(
        URI.create(((ServletWebRequest) request).getRequest().getRequestURI()));
    problemDetail.setProperty("timestamp", Instant.now());
    return problemDetail;
  }

  // --- CONFLICT (409) ---
  //  @ExceptionHandler({DuplicateRecord.class, EmailAlreadyExistException.class})
  //  public ProblemDetail handleConflictExceptions(RuntimeException ex, WebRequest request) {
  //    ProblemDetail problemDetail =
  //        createProblemDetail(HttpStatus.CONFLICT, ex.getMessage(), request);
  //    problemDetail.setTitle("Conflict");
  //    return problemDetail;
  //  }

  // --- BAD REQUEST (400) ---
  // Consolidated multiple bad-request type exceptions
  //  @ExceptionHandler({
  //    BadRequestException.class,
  //    GithubOauthException.class,
  //    GithubApiException.class,
  //    IllegalArgumentException.class
  //  })
  //  public ProblemDetail handleGenericBadRequestExceptions(RuntimeException ex, WebRequest
  // request) {
  //    ProblemDetail problemDetail =
  //        createProblemDetail(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
  //    problemDetail.setTitle("Bad Request");
  //    return problemDetail;
  //  }

  @ExceptionHandler(BadRequestException.class)
  public ProblemDetail handleBadException(BadRequestException e) {
    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
    problemDetail.setProperty("timestamp", LocalDateTime.now());
    return problemDetail;
  }

//  @ExceptionHandler(BadCredentialsException.class)
//  public ProblemDetail handleBadCredentials(BadCredentialsException ex, WebRequest request) {
//    ProblemDetail problemDetail =
//        createProblemDetail(HttpStatus.UNAUTHORIZED, "Invalid username or password.", request);
//    problemDetail.setTitle("Authentication Failed");
//    return problemDetail;
//  }

  // --- NOT FOUND (404) ---
  @ExceptionHandler(NotFoundException.class)
  public ProblemDetail handleNotFoundException(NotFoundException ex, WebRequest request) {
    ProblemDetail problemDetail =
        createProblemDetail(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    problemDetail.setTitle("Resource Not Found");
    return problemDetail;
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ProblemDetail handleDataIntegrityViolation(DataIntegrityViolationException ex) {
    // Default response
    ProblemDetail problemDetail =
        ProblemDetail.forStatusAndDetail(
            HttpStatus.CONFLICT, "Duplicate value violates unique constraint.");

    problemDetail.setTitle("Data Integrity Violation");
    problemDetail.setProperty("errorCode", "EMAIL_ALREADY_EXISTS");

    // Optional: extract the offending field (email)
    String message = ex.getRootCause() != null ? ex.getRootCause().getMessage() : ex.getMessage();
    if (message != null && message.contains("email")) {
      problemDetail.setDetail("Email already exists in the system.");
    }

    return problemDetail;
  }

  @ExceptionHandler(MalformedJwtException.class)
  public ProblemDetail handleMalformedJwtException(MalformedJwtException ex, WebRequest request) {
    ProblemDetail problemDetail =
        createProblemDetail(
            HttpStatus.UNAUTHORIZED, "Invalid authentication token. Please log in again.", request);
    problemDetail.setTitle("Invalid Token");
    return problemDetail;
  }

  // --- FORBIDDEN (403) & UNAUTHORIZED (401) ---
//  @ExceptionHandler(AccessDeniedException.class)
//  public ProblemDetail handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
//    ProblemDetail problemDetail =
//        createProblemDetail(
//            HttpStatus.FORBIDDEN, "You do not have permission to access this resource.", request);
//    problemDetail.setTitle("Access Denied");
//    return problemDetail;
//  }

  //  @ExceptionHandler(ExpiredJwtException.class)
  //  public ProblemDetail handleExpiredJwtException(ExpiredJwtException ex, WebRequest request) {
  //    ProblemDetail problemDetail =
  //        createProblemDetail(
  //            HttpStatus.UNAUTHORIZED, "Your session has expired. Please log in again.", request);
  //    problemDetail.setTitle("Session Expired");
  //    return problemDetail;
  //  }

  // --- VALIDATION ERRORS (400) ---
  // Harmonized validation error responses

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ProblemDetail handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex, WebRequest request) {
    ProblemDetail problemDetail =
        createProblemDetail(
            HttpStatus.BAD_REQUEST, "Validation failed. Please check your input.", request);
    problemDetail.setTitle("Validation Error");

    List<Object> errors = new ArrayList<>();
    for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
      errors.add(
          new ValidationError(
              fieldError.getField(),
              fieldError.getDefaultMessage(),
              Objects.toString(fieldError.getRejectedValue(), "null")));
    }
    problemDetail.setProperty("errors", errors);
    return problemDetail;
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ProblemDetail handleConstraintViolation(
      ConstraintViolationException ex, WebRequest request) {
    ProblemDetail problemDetail =
        createProblemDetail(
            HttpStatus.BAD_REQUEST, "Validation failed. Please check your input.", request);
    problemDetail.setTitle("Validation Error");

    List<Object> errors = new ArrayList<>();
    for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
      // Extracts the field name from the property path
      String path = violation.getPropertyPath().toString();
      String fieldName = path.substring(path.lastIndexOf('.') + 1);
      errors.add(
          new ValidationError(
              fieldName,
              violation.getMessage(),
              Objects.toString(violation.getInvalidValue(), "null")));
    }
    problemDetail.setProperty("errors", errors);
    return problemDetail;
  }

  // A simple record to structure validation errors consistently
  private record ValidationError(String field, String message, String rejectedValue) {}

  // --- CATCH-ALL (500) ---
  // Safety net for any unhandled exceptions
  @ExceptionHandler(Exception.class)
  public ProblemDetail handleAllUncaughtException(Exception ex, WebRequest request) {
    // Log the full stack trace for debugging purposes
    logger.error("An unexpected error occurred", ex);

    ProblemDetail problemDetail =
        createProblemDetail(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "An unexpected internal server error has occurred.",
            request);
    problemDetail.setTitle("Internal Server Error");
    return problemDetail;
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<Object> handleMalformedJson(HttpMessageNotReadableException ex) {
    // Customize your response
    Map<String, Object> body = new LinkedHashMap<>();
    body.put("timestamp", LocalDateTime.now());
    body.put("status", HttpStatus.BAD_REQUEST.value());
    body.put("error", "Malformed JSON request");
    body.put("message", ex.getMostSpecificCause().getMessage());

    return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
  }
}
