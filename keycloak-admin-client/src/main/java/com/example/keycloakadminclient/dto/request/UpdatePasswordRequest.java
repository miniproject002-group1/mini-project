package com.example.keycloakadminclient.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdatePasswordRequest {
  @NotBlank
  private String newPassword;
}