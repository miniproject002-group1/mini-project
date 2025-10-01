package com.example.productservice.client;

import com.example.productservice.model.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;


@FeignClient(name = "keycloak-admin-client", path = "/api/v1/profiles/user")
public interface UserClient {
    @GetMapping
    UserResponse getCurrentUser(@RequestHeader("X-User-Id") String userId);
}
