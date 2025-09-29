package com.example.keycloakadminclient.controller;


import com.example.keycloakadminclient.dto.request.UserCreateRequest;
import com.example.keycloakadminclient.service.IKeycloakAdminClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class KeycloakAdminClientController {

  private final IKeycloakAdminClientService keycloakAdminClientService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public void createUser(@RequestBody UserCreateRequest userCreateRequest) {
    keycloakAdminClientService.createUser(userCreateRequest);
  }
}
