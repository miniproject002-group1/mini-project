package com.example.keycloakadminclient.service.impl;

import com.example.keycloakadminclient.dto.request.LoginRequest;
import com.example.keycloakadminclient.dto.request.UpdateAppUserRequest;
import com.example.keycloakadminclient.dto.request.UpdatePasswordRequest;
import com.example.keycloakadminclient.dto.request.UserCreateRequest;
import com.example.keycloakadminclient.dto.response.AppUserResponse;
import com.example.keycloakadminclient.dto.response.LoginResponse;
import com.example.keycloakadminclient.service.IAuthService;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService implements IAuthService {

  private final Keycloak keycloak;
  private final RestTemplate restTemplate = new RestTemplate();

  @Value("${app.keycloak.realm}")
  private String realm;
  @Value("${app.keycloak.client-id}")
  private String clientId;
  @Value("${app.keycloak.client-secret}")
  private String clientSecret;
  @Value("${app.keycloak.server-url}")
  private String keycloakServerUrl;

  private static final String TOKEN_URL = "/protocol/openid-connect/token";
  private static final String LOGOUT_URL = "/protocol/openid-connect/logout";

  @Override
  public void createUser(UserCreateRequest userCreateRequest) {
    UserRepresentation user = new UserRepresentation();
    user.setEnabled(true);
    user.setUsername(userCreateRequest.getUsername());
    user.setFirstName(userCreateRequest.getFirstName());
    user.setLastName(userCreateRequest.getLastName());
    user.setEmail(userCreateRequest.getEmail());
    user.setEmailVerified(true);

    CredentialRepresentation credential = new CredentialRepresentation();
    credential.setTemporary(false);
    credential.setType(CredentialRepresentation.PASSWORD);
    credential.setValue(userCreateRequest.getPassword());
    user.setCredentials(Collections.singletonList(credential));

    UsersResource usersResource = keycloak.realm(realm).users();
    try (Response response = usersResource.create(user)) {
      if (response.getStatus() == 201) {
        log.info("User {} created successfully in Keycloak", user.getUsername());
      } else {
        log.error("Error creating user {}, status: {}. Reason: {}", user.getUsername(), response.getStatus(), response.getStatusInfo().getReasonPhrase());
      }
    }
  }

  @Override
  public LoginResponse login(LoginRequest request) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    map.add("grant_type", "password");
    map.add("client_id", clientId);
    map.add("client_secret", clientSecret);
    map.add("username", request.getUsername());
    map.add("password", request.getPassword());

    return executeTokenRequest(map);
  }

  @Override
  public LoginResponse refresh(String refreshToken) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    map.add("grant_type", "refresh_token");
    map.add("client_id", clientId);
    map.add("client_secret", clientSecret);
    map.add("refresh_token", refreshToken);

    return executeTokenRequest(map);
  }

  @Override
  public void logout(String refreshToken) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
    map.add("client_id", clientId);
    map.add("client_secret", clientSecret);
    map.add("refresh_token", refreshToken);

    HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map, headers);
    restTemplate.postForEntity(keycloakServerUrl + "/realms/" + realm + LOGOUT_URL, httpEntity, String.class);
    log.info("User session invalidated successfully.");
  }

  @Override
  public AppUserResponse getCurrentUserProfile(String userId) {
    UserResource userResource = keycloak.realm(realm).users().get(userId);
    UserRepresentation userRepresentation = userResource.toRepresentation();

    return AppUserResponse.builder()
            .id(userRepresentation.getId())
            .username(userRepresentation.getUsername())
            .email(userRepresentation.getEmail())
            .firstName(userRepresentation.getFirstName())
            .lastName(userRepresentation.getLastName())
            .build();
  }

  @Override
  public AppUserResponse updateCurrentUserProfile(String userId, UpdateAppUserRequest request) {
    UserResource userResource = keycloak.realm(realm).users().get(userId);
    UserRepresentation userRepresentation = userResource.toRepresentation();

    // Update fields
    userRepresentation.setFirstName(request.getFirstName());
    userRepresentation.setLastName(request.getLastName());
    userRepresentation.setEmail(request.getEmail());

    userResource.update(userRepresentation);
    log.info("User profile for {} updated.", userId);

    return getCurrentUserProfile(userId); // Return the updated profile
  }

  @Override
  public void updateUserPassword(String userId, UpdatePasswordRequest request) {
    UserResource userResource = keycloak.realm(realm).users().get(userId);

    CredentialRepresentation credential = new CredentialRepresentation();
    credential.setTemporary(false);
    credential.setType(CredentialRepresentation.PASSWORD);
    credential.setValue(request.getNewPassword());

    userResource.resetPassword(credential);
    log.info("Password for user {} updated.", userId);
  }

  private LoginResponse executeTokenRequest(MultiValueMap<String, String> map) {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(map, headers);
    ResponseEntity<Map> response = restTemplate.exchange(
            keycloakServerUrl + "/realms/" + realm + TOKEN_URL,
            HttpMethod.POST,
            httpEntity,
            Map.class
    );

    Map<String, Object> responseBody = response.getBody();
    if (responseBody != null) {
      return LoginResponse.builder()
              .accessToken((String) responseBody.get("access_token"))
              .refreshToken((String) responseBody.get("refresh_token"))
              .expiresIn(((Number) responseBody.get("expires_in")).longValue())
              .build();
    }
    return null;
  }
}