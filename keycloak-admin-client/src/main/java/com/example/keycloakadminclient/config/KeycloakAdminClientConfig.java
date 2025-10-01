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
    return KeycloakBuilder.builder().serverUrl(keycloakConfigProperties.getServerUrl()).realm(keycloakConfigProperties.getRealm()).grantType(OAuth2Constants.CLIENT_CREDENTIALS) // use client credentials
            .clientId(keycloakConfigProperties.getClientId()).clientSecret(keycloakConfigProperties.getClientSecret()) // add this
            .build();
  }
}
