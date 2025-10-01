package com.example.keycloakadminclient.service;

import com.example.keycloakadminclient.dto.request.UserCreateRequest;

public interface IAuthService {
  void createUser(UserCreateRequest userCreateRequest);
}
