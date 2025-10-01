package com.example.keycloakadminclient.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    // This configuration allows the gateway to successfully forward requests
    // from the browser to this service.
    registry.addMapping("/api/**") // Apply to all API paths
            .allowedOrigins("*")     // Allow requests from any origin
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allowed methods
            .allowedHeaders("*");      // Allow all headers
  }
}