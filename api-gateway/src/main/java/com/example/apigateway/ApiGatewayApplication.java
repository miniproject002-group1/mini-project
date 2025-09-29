package com.example.apigateway;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

// ADD THESE ANNOTATIONS
@OpenAPIDefinition(
        info = @Info(title = "API Gateway", version = "v1"),
        // This tells Swagger to apply the security scheme globally to all endpoints
        security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
        name = "bearerAuth", // A name for the security scheme
        type = SecuritySchemeType.HTTP, // The type of security
        scheme = "bearer", // The scheme used (i.e., Bearer authentication)
        bearerFormat = "JWT" // The format of the bearer token
)
@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayApplication {

  public static void main(String[] args) {
    SpringApplication.run(ApiGatewayApplication.class, args);
  }

}
