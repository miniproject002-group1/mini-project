package com.example.keycloakadminclient.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AppUserResponse {
  private String id;
  private String username;
  private String email;
  private String firstName;
  private String lastName;
}