package com.example.categoryservice.dto.response;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppUserResponse {

    private UUID userId;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String imageUrl;

}
