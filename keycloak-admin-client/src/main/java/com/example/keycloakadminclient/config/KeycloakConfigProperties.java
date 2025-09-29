package com.example.keycloakadminclient.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "app.keycloak")
public class KeycloakConfigProperties {
  private String serverUrl;
  private String realm;
  private String clientId;
  private String username;
  private String password;
}
