package com.example.apigateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

//  @Bean
//  public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
//    return builder.routes()
//            // Route for Category Service API
//            .route("category-service-route", r -> r.path("/api/v1/categories/**")
//                    .uri("lb://category-service"))
//
//            // Route for Keycloak Admin Client API
//            .route("keycloak-admin-client-route", r -> r.path("/api/v1/users/**")
//                    .uri("lb://keycloak-admin-client"))
//
//            // --- NEW: Route for Category Service API Docs ---
//            .route("category-service-docs-route", r -> r.path("/v3/api-docs/category-service")
//                    .filters(f -> f.rewritePath("/v3/api-docs/category-service", "/v3/api-docs"))
//                    .uri("lb://category-service"))
//
//            // --- NEW: Route for Keycloak Admin Client API Docs ---
//            .route("keycloak-admin-client-docs-route", r -> r.path("/v3/api-docs/keycloak-admin-client")
//                    .filters(f -> f.rewritePath("/v3/api-docs/keycloak-admin-client", "/v3/api-docs"))
//                    .uri("lb://keycloak-admin-client"))
//            .build();
//  }
}
