package com.example.keycloakadminclient.controller;

import com.example.keycloakadminclient.dto.request.LoginRequest;
import com.example.keycloakadminclient.dto.request.UserCreateRequest;
import com.example.keycloakadminclient.dto.response.ApiResponse;
import com.example.keycloakadminclient.dto.response.BaseResponse;
import com.example.keycloakadminclient.service.IAuthService;
import com.example.keycloakadminclient.service.impl.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "Auth")
@RestController
@RequestMapping("/api/v1/auths")
@RequiredArgsConstructor
public class AuthController extends BaseResponse {

  private final AuthService authService;

  @Operation(summary = "Login user with Keycloak")
  @PostMapping("/login")
  public ResponseEntity<ApiResponse<Map<String, Object>>> login(@RequestBody LoginRequest request) {
    Map<String, Object> tokens = authService.login(request);
    return responseEntity(true, "Login successful", HttpStatus.OK, tokens);
  }


  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public void createUser(@RequestBody UserCreateRequest userCreateRequest) {
    authService.createUser(userCreateRequest);
  }


}