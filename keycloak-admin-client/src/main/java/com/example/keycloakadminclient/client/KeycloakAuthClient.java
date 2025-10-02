package com.example.keycloakadminclient.client;

import com.example.keycloakadminclient.config.FeignFormConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(
        name = "keycloak-auth-client",
        configuration = FeignFormConfig.class // Use our custom form encoder config
)
public interface KeycloakAuthClient {

  @PostMapping(
          value = "/realms/{realm}/protocol/openid-connect/token",
          consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
  )
  Map<String, Object> token(@PathVariable("realm") String realm, @RequestBody Map<String, ?> form);

  @PostMapping(
          value = "/realms/{realm}/protocol/openid-connect/logout",
          consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
  )
  void logout(@PathVariable("realm") String realm, @RequestBody Map<String, ?> form);
}