package com.example.apigateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

  @Bean
  public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
    return builder.routes()
            .route("category-service-route", r -> r.path("/api/v1/categories/**")
                    .uri("lb://category-service"))
            // You can add more routes here for other services
            // .route("product-service-route", r -> r.path("/api/v1/products/**")
            //         .uri("lb://product-service"))
            .build();
  }
}
