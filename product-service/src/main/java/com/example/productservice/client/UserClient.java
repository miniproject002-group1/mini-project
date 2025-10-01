package com.example.productservice.client;

import com.example.productservice.model.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "USER-SERVICE", path = "/api/v1/users")
public interface UserClient {
    @GetMapping("/{id}")
    UserResponse getUserById(@PathVariable UUID id);
}
