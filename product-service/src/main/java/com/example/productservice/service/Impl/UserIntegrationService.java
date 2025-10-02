package com.example.productservice.service.Impl;

import com.example.productservice.client.UserClient;

import com.example.productservice.exception.BadRequestException;
import com.example.productservice.exception.NotFoundException;
//import com.example.productservice.exception.ServiceUnavailableException;
import com.example.productservice.model.response.AppUserResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserIntegrationService {

    private final UserClient userClient;

    @CircuitBreaker(name = "userService", fallbackMethod = "userFallback")
    public AppUserResponse getUserByUserId(String userId) {
        AppUserResponse appUserResponse = userClient.getCurrentUser(userId);
        if(appUserResponse == null || appUserResponse.getUserId() == null) {
            throw new NotFoundException("User Id not found");
        }
        return appUserResponse;
    }

    public AppUserResponse userFallback(Throwable ex) {
        throw new BadRequestException(
                "User Service unavailable : "+ex.getMessage());
    }

}
