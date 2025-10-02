package com.example.categoryservice.client;

import com.example.categoryservice.dto.response.ApiResponse;
import com.example.categoryservice.dto.response.AppUserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "keycloak-admin-client",
        path = "/api/v1/profiles"
)
public interface UserClient {

    @GetMapping
    ApiResponse<AppUserResponse> getCurrentUserProfile();

}
