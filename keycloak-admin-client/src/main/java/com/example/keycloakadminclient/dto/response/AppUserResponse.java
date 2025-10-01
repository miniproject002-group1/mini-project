package com.example.keycloakadminclient.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class AppUserResponse {
  private UUID id;
  private String username;
  private String email;
  private String firstName;
  private String lastName;
}