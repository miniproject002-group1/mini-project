package com.example.keycloakadminclient.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
            .csrf(csrf -> csrf.disable()) // Disable CSRF as we are using token-based authentication (stateless)
            .authorizeHttpRequests(authorize -> authorize
                    // 1. Public endpoints for Swagger/OpenAPI
                    .requestMatchers(
                            "/v3/api-docs/**",
                            "/swagger-ui/**",
                            "/swagger-ui.html"
                    ).permitAll()

                    // 2. Public API endpoints for authentication and registration
                    .requestMatchers(HttpMethod.POST, "/api/v1/auths/**").permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/v1/profiles/register").permitAll()

                    // 3. All other requests must be authenticated
                    .anyRequest().authenticated()
            )
            // Configure the app as an OAuth2 Resource Server to validate JWTs
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(withDefaults()))

            // Make the session management stateless
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    return http.build();
  }
}