package com.example.keycloakadminclient.service;

import com.example.keycloakadminclient.dto.request.UserCreateRequest;

import com.example.keycloakadminclient.dto.request.UpdateAppUserRequest;
import com.example.keycloakadminclient.dto.request.UpdatePasswordRequest;
import com.example.keycloakadminclient.dto.request.UserCreateRequest;
import com.example.keycloakadminclient.dto.response.AppUserResponse;
import com.example.keycloakadminclient.dto.response.LoginResponse;

public interface IAuthService {
  // User Creation
  void createUser(UserCreateRequest userCreateRequest);

  // Auth Actions
  LoginResponse login(com.example.keycloakadminclient.dto.request.LoginRequest request);

  LoginResponse refresh(String refreshToken);

  void logout(String refreshToken);

  // Profile Management
  AppUserResponse getCurrentUserProfile(String userId);

  AppUserResponse updateCurrentUserProfile(String userId, UpdateAppUserRequest request);

  void updateUserPassword(String userId, UpdatePasswordRequest request);
}
