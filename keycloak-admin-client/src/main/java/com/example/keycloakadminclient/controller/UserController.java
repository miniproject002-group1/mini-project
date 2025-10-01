package com.example.keycloakadminclient.controller;

import com.example.keycloakadminclient.dto.request.UpdateAppUserRequest;
import com.example.keycloakadminclient.dto.request.UpdatePasswordRequest;
import com.example.keycloakadminclient.dto.request.UserCreateRequest;
import com.example.keycloakadminclient.dto.response.ApiResponse;
import com.example.keycloakadminclient.dto.response.AppUserResponse;
import com.example.keycloakadminclient.dto.response.BaseResponse;
import com.example.keycloakadminclient.service.impl.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement; // Make sure this is imported
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "User")
@RestController
@RequestMapping("/api/v1/profiles")
@RequiredArgsConstructor
public class UserController extends BaseResponse {

  private final AuthService authService;

  // This is a public endpoint, so NO lock icon is needed.
  @Operation(summary = "Register a new user")
  @PostMapping("/register")
  public ResponseEntity<ApiResponse<Void>> register(@RequestBody @Valid UserCreateRequest userCreateRequest) {
    authService.createUser(userCreateRequest);
    return responseEntity(true, "User registered successfully.", HttpStatus.CREATED);
  }

  // 👇 ADD THIS ANNOTATION
  @SecurityRequirement(name = "bearerAuth")
  @Operation(summary = "Get current user's information")
  @GetMapping
  public ResponseEntity<ApiResponse<AppUserResponse>> getCurrentUserProfile(@AuthenticationPrincipal Jwt jwt) {
    UUID userId = UUID.fromString(jwt.getSubject());
    return responseEntity(true, "User profile retrieved successfully.", HttpStatus.OK, authService.getCurrentUserProfile(userId));
  }

  // 👇 ADD THIS ANNOTATION
  @SecurityRequirement(name = "bearerAuth")
  @Operation(summary = "Update current user's information")
  @PutMapping
  public ResponseEntity<ApiResponse<AppUserResponse>> updateCurrentUserProfile(@AuthenticationPrincipal Jwt jwt, @RequestBody @Valid UpdateAppUserRequest request) {
    UUID userId = UUID.fromString(jwt.getSubject());
    return responseEntity(true, "User profile updated successfully.", HttpStatus.OK, authService.updateCurrentUserProfile(userId, request));
  }

  // 👇 ADD THIS ANNOTATION
  @SecurityRequirement(name = "bearerAuth")
  @Operation(summary = "Change current user's password")
  @PutMapping("/password")
  public ResponseEntity<ApiResponse<Void>> changeUserPassword(@AuthenticationPrincipal Jwt jwt, @RequestBody @Valid UpdatePasswordRequest request) {
    UUID userId = UUID.fromString(jwt.getSubject());
    authService.updateUserPassword(userId, request);
    return responseEntity(true, "User password updated successfully.", HttpStatus.OK);
  }


  //get Current user for feign
  @GetMapping("/user")
  public AppUserResponse getCurrentUser(@RequestHeader("X-User-Id") String userId) {
    return authService.getCurrentUserProfile(UUID.fromString(userId));
  }

}