package com.example.categoryservice.entity;

import com.example.categoryservice.dto.response.AppUserResponse;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "user_id", updatable = false, nullable = false)
    private UUID userId;

    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private String imageUrl;

    public AppUserResponse toResponse() {
        return AppUserResponse.builder()
                .userId(userId)
                .firstName(firstName)
                .lastName(lastName)
                .username(username)
                .email(email)
//                .imageUrl(imageUrl)
                .build();
    }

}
