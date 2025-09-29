package com.example.keycloakadminclient.service.impl;

import com.example.keycloakadminclient.dto.request.UserCreateRequest;
import com.example.keycloakadminclient.service.IKeycloakAdminClientService;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class KeycloakAdminClientService implements IKeycloakAdminClientService {
  private final Keycloak keycloak;

  public void createUser(UserCreateRequest userCreateRequest) {
    // Define the user
    UserRepresentation user = new UserRepresentation();
    user.setEnabled(true);
    user.setUsername(userCreateRequest.getUsername());
    user.setFirstName(userCreateRequest.getFirstName());
    user.setLastName(userCreateRequest.getLastName());
    user.setEmail(userCreateRequest.getEmail());
    user.setEmailVerified(true);

    // Define the password
    CredentialRepresentation credential = new CredentialRepresentation();
    credential.setTemporary(false);
    credential.setType(CredentialRepresentation.PASSWORD);
    credential.setValue(userCreateRequest.getPassword());
    user.setCredentials(Collections.singletonList(credential));

    // Get the users resource and create the user
    UsersResource usersResource = keycloak.realm("spring-microservices-realm").users();
    try (Response response = usersResource.create(user)) {
      log.info("Keycloak user creation response: {}", response.getStatusInfo().getReasonPhrase());
      if (response.getStatus() == 201) {
        log.info("User {} created successfully in Keycloak", user.getUsername());
      } else if (response.getStatus() == 409) {
        log.error("User {} already exists in Keycloak", user.getUsername());
        // Optionally throw a custom exception
      } else {
        log.error("Error creating user in Keycloak, status: {}", response.getStatus());
      }
    }
  }
}
