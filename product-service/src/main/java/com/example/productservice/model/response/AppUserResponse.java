package com.example.productservice.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppUserResponse {
    private UUID userId;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String imageUrl;
}
