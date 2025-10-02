package com.example.keycloakadminclient.service.impl;

import com.example.keycloakadminclient.client.KeycloakAuthClient;
import com.example.keycloakadminclient.dto.request.LoginRequest;
import com.example.keycloakadminclient.dto.request.UpdateAppUserRequest;
import com.example.keycloakadminclient.dto.request.UpdatePasswordRequest;
import com.example.keycloakadminclient.dto.request.UserCreateRequest;
import com.example.keycloakadminclient.dto.response.AppUserResponse;
import com.example.keycloakadminclient.dto.response.LoginResponse;
import com.example.keycloakadminclient.exception.InvalidCredentialsException;
import com.example.keycloakadminclient.exception.UserAlreadyExistsException;
import com.example.keycloakadminclient.service.IAuthService;
import feign.FeignException;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService implements IAuthService {

  private final Keycloak keycloak;
  private final KeycloakAuthClient keycloakAuthClient;

  @Value("${app.keycloak.realm}")
  private String realm;
  @Value("${app.keycloak.client-id}")
  private String clientId;
  @Value("${app.keycloak.client-secret}")
  private String clientSecret;

  @Override
  public void createUser(UserCreateRequest userCreateRequest) {
    UserRepresentation user = new UserRepresentation();
    user.setEnabled(true);
    user.setUsername(userCreateRequest.getUsername());
    user.setFirstName(userCreateRequest.getFirstName());
    user.setLastName(userCreateRequest.getLastName());
    user.setEmail(userCreateRequest.getEmail());
    user.setEmailVerified(true);

    // Handles setting the imageUrl as a custom user attribute
    if (userCreateRequest.getImageUrl() != null && !userCreateRequest.getImageUrl().isEmpty()) {
      Map<String, List<String>> attributes = new HashMap<>();
      attributes.put("imageUrl", Collections.singletonList(userCreateRequest.getImageUrl()));
      user.setAttributes(attributes);
    }

    CredentialRepresentation credential = new CredentialRepresentation();
    credential.setTemporary(false);
    credential.setType(CredentialRepresentation.PASSWORD);
    credential.setValue(userCreateRequest.getPassword());
    user.setCredentials(Collections.singletonList(credential));

    UsersResource usersResource = keycloak.realm(realm).users();
    try (Response response = usersResource.create(user)) {
      if (response.getStatus() == 409) {
        throw new UserAlreadyExistsException("User '" + userCreateRequest.getUsername() + "' already exists.");
      }
      if (response.getStatus() == 201) {
        log.info("User {} created successfully in Keycloak", user.getUsername());
      } else {
        log.error("Error creating user {}, status: {}. Reason: {}", user.getUsername(), response.getStatus(), response.getStatusInfo().getReasonPhrase());
        throw new RuntimeException("Error creating user in Keycloak.");
      }
    }
  }

  @Override
  public LoginResponse login(LoginRequest request) {
    Map<String, String> form = new HashMap<>();
    form.put("grant_type", "password");
    form.put("client_id", clientId);
    form.put("client_secret", clientSecret);
    // Handles logging in with email instead of username
    form.put("scope", "openid");
    form.put("username", request.getEmail());
    form.put("password", request.getPassword());

    try {
      Map<String, Object> responseBody = keycloakAuthClient.token(realm, form);
      return mapToLoginResponse(responseBody);
    } catch (FeignException.Unauthorized | FeignException.BadRequest e) {
      throw new InvalidCredentialsException("Invalid username or password.");
    }
  }

  @Override
  public AppUserResponse getCurrentUserProfile(UUID userId) {
    try {
      UserResource userResource = keycloak.realm(realm).users().get(userId.toString());
      UserRepresentation userRepresentation = userResource.toRepresentation();

      // Handles retrieving the imageUrl from custom attributes
      String imageUrl = null;
      if (userRepresentation.getAttributes() != null && userRepresentation.getAttributes().containsKey("imageUrl")) {
        imageUrl = userRepresentation.getAttributes().get("imageUrl").get(0);
      }

      return AppUserResponse.builder()
              .userId(UUID.fromString(userRepresentation.getId()))
              .username(userRepresentation.getUsername())
              .email(userRepresentation.getEmail())
              .firstName(userRepresentation.getFirstName())
              .lastName(userRepresentation.getLastName())
              .imageUrl(imageUrl)
              .build();
    } catch (NotFoundException e) {
      throw new NotFoundException("User with ID '" + userId + "' not found.");
    }
  }

  @Override
  public AppUserResponse updateCurrentUserProfile(UUID userId, UpdateAppUserRequest request) {
    try {
      UserResource userResource = keycloak.realm(realm).users().get(userId.toString());
      UserRepresentation userRepresentation = userResource.toRepresentation();

      userRepresentation.setFirstName(request.getFirstName());
      userRepresentation.setLastName(request.getLastName());
      userRepresentation.setEmail(request.getEmail());

      // Handles updating the imageUrl as a custom attribute
      Map<String, List<String>> attributes = userRepresentation.getAttributes();
      if (attributes == null) {
        attributes = new HashMap<>();
      }
      attributes.put("imageUrl", Collections.singletonList(request.getImageUrl()));
      userRepresentation.setAttributes(attributes);

      userResource.update(userRepresentation);
      log.info("User profile for {} updated.", userId);

      return getCurrentUserProfile(userId);
    } catch (NotFoundException e) {
      throw new NotFoundException("User with ID '" + userId + "' not found.");
    }
  }

  @Override
  public LoginResponse refresh(String refreshToken) {
    Map<String, String> form = new HashMap<>();
    form.put("grant_type", "refresh_token");
    form.put("client_id", clientId);
    form.put("client_secret", clientSecret);
    form.put("refresh_token", refreshToken);

    try {
      Map<String, Object> responseBody = keycloakAuthClient.token(realm, form);
      return mapToLoginResponse(responseBody);
    } catch (FeignException.BadRequest e) {
      throw new InvalidCredentialsException("Invalid or expired refresh token.");
    }
  }

  @Override
  public void logout(String refreshToken) {
    Map<String, String> form = new HashMap<>();
    form.put("client_id", clientId);
    form.put("client_secret", clientSecret);
    form.put("refresh_token", refreshToken);

    try {
      keycloakAuthClient.logout(realm, form);
      log.info("User session invalidated successfully.");
    } catch (Exception e) {
      log.error("Error during logout", e);
    }
  }

  @Override
  public void updateUserPassword(UUID userId, UpdatePasswordRequest request) {
    try {
      UserResource userResource = keycloak.realm(realm).users().get(userId.toString());

      CredentialRepresentation credential = new CredentialRepresentation();
      credential.setTemporary(false);
      credential.setType(CredentialRepresentation.PASSWORD);
      credential.setValue(request.getNewPassword());

      userResource.resetPassword(credential);
      log.info("Password for user {} updated.", userId);
    } catch (NotFoundException e) {
      throw new NotFoundException("User with ID '" + userId + "' not found.");
    }
  }

  // Handles mapping the full token response from Keycloak
  private LoginResponse mapToLoginResponse(Map<String, Object> responseBody) {
    if (responseBody != null) {
      return LoginResponse.builder()
              .accessToken((String) responseBody.get("access_token"))
              .expiresIn(((Number) responseBody.get("expires_in")).longValue())
              .refreshExpiresIn(((Number) responseBody.get("refresh_expires_in")).longValue())
              .refreshToken((String) responseBody.get("refresh_token"))
              .tokenType((String) responseBody.get("token_type"))
              .idToken((String) responseBody.get("id_token"))
              .notBeforePolicy(((Number) responseBody.get("not-before-policy")).intValue())
              .sessionState((String) responseBody.get("session_state"))
              .scope((String) responseBody.get("scope"))
              .build();
    }
    return null;
  }
}