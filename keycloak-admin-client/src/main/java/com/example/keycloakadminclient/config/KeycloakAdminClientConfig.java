package com.example.keycloakadminclient.config;

import lombok.RequiredArgsConstructor;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class KeycloakAdminClientConfig {

  private final KeycloakConfigProperties keycloakConfigProperties;

  @Bean
  public Keycloak keycloak() {
    return KeycloakBuilder.builder()
            .serverUrl(keycloakConfigProperties.getServerUrl())
            .realm(keycloakConfigProperties.getRealm())
            .grantType(OAuth2Constants.PASSWORD) // Using password grant type for admin credentials
            .clientId(keycloakConfigProperties.getClientId())
            .username(keycloakConfigProperties.getUsername())
            .password(keycloakConfigProperties.getPassword())
            .build();
  }
}
