package com.example.keycloakadminclient.controller;

import com.example.keycloakadminclient.dto.request.LoginRequest;
import com.example.keycloakadminclient.dto.response.ApiResponse;
import com.example.keycloakadminclient.dto.response.BaseResponse;
import com.example.keycloakadminclient.dto.response.LoginResponse;
import com.example.keycloakadminclient.service.impl.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Authentication")
@RestController
@RequestMapping("/api/v1/auths")
@RequiredArgsConstructor
public class AuthController extends BaseResponse {

  private final AuthService authService;

  @PostMapping("/login")
  public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody @Valid LoginRequest request) {
    return responseEntity(true, "Login successful", HttpStatus.OK, authService.login(request));
  }

  @PostMapping("/refresh")
  public ResponseEntity<ApiResponse<LoginResponse>> refresh(@RequestParam String refreshToken) {
    return responseEntity(true, "Token refreshed successfully", HttpStatus.OK, authService.refresh(refreshToken));
  }

  @PostMapping("/logout")
  public ResponseEntity<ApiResponse<String>> logout(@RequestParam String refreshToken) {
    authService.logout(refreshToken);
    return responseEntity(true, "Logout successful", HttpStatus.OK, "You have been logged out.");
  }
}