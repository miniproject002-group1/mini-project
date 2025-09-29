package com.example.keycloakadminclient.service;

import com.example.keycloakadminclient.dto.request.UserCreateRequest;

public interface IKeycloakAdminClientService {
  void createUser(UserCreateRequest userCreateRequest);
}
