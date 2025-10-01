package com.example.keycloakadminclient;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@OpenAPIDefinition(
        info = @Info(title = "Keycloak Service API", version = "v1"),
        servers = {@Server(url = "/")}
)
@SpringBootApplication
@EnableDiscoveryClient
public class KeycloakAdminClientApplication {

  public static void main(String[] args) {
    SpringApplication.run(KeycloakAdminClientApplication.class, args);
  }

}
