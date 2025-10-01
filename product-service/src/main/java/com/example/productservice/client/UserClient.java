package com.example.productservice.client;

import com.example.productservice.model.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "user-service")
public interface UserClient {
    @GetMapping("/{id}")
    UserResponse getUserById(Long id);
}
