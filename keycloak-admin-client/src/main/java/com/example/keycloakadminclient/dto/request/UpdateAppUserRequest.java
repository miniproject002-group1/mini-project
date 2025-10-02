package com.example.keycloakadminclient.dto.request;

import lombok.Data;

@Data
public class UpdateAppUserRequest {
  private String firstName;
  private String lastName;
  private String email;
  private String imageUrl;
}