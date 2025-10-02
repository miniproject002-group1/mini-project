package com.example.productservice.service.Impl;

import com.example.productservice.client.UserClient;

import com.example.productservice.exception.BadRequestException;
import com.example.productservice.exception.NotFoundException;
import com.example.productservice.exception.ServiceUnavailableException;
import com.example.productservice.model.response.UserResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
@RequiredArgsConstructor
public class UserIntegrationService {

    private final UserClient userClient;

    @CircuitBreaker(name = "userService", fallbackMethod = "userFallback")
    public UserResponse getUserByUserId(String userId) {
        UserResponse userResponse = userClient.getCurrentUser(userId);
        if(userResponse == null ||userResponse.getId()== null) {
            throw new NotFoundException("User Id not found");
        }
        return userResponse;
    }

    public UserResponse userFallback(Throwable ex) {
        throw new ServiceUnavailableException(
                "User Service unavailable : "+ex.getMessage());
    }

}
