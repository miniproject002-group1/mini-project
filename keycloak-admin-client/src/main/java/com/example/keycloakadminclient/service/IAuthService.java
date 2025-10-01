package com.example.keycloakadminclient.service;

import com.example.keycloakadminclient.dto.request.UserCreateRequest;

import com.example.keycloakadminclient.dto.request.UpdateAppUserRequest;
import com.example.keycloakadminclient.dto.request.UpdatePasswordRequest;
import com.example.keycloakadminclient.dto.request.UserCreateRequest;
import com.example.keycloakadminclient.dto.response.AppUserResponse;
import com.example.keycloakadminclient.dto.response.LoginResponse;

import java.util.UUID;

public interface IAuthService {
  // User Creation
  void createUser(UserCreateRequest userCreateRequest);

  // Auth Actions
  LoginResponse login(com.example.keycloakadminclient.dto.request.LoginRequest request);

  LoginResponse refresh(String refreshToken);

  void logout(String refreshToken);

  // Profile Management
  AppUserResponse getCurrentUserProfile(UUID userId);

  AppUserResponse updateCurrentUserProfile(UUID userId, UpdateAppUserRequest request);

  void updateUserPassword(UUID userId, UpdatePasswordRequest request);

}
