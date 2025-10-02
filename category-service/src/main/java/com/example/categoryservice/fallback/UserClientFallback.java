package com.example.categoryservice.fallback;//package com.kshrd.beforemini.fallback;
//
//import com.kshrd.beforemini.client.UserClient;
//import com.kshrd.beforemini.dto.response.ApiResponse;
//import com.kshrd.beforemini.dto.response.UserResponse;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Component;
//
//import java.time.LocalDateTime;
//
//@Component
//public class UserClientFallback implements UserClient{
//
//    @Override
//    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser() {
//        UserResponse fallbackUser = UserResponse.builder()
//                .userId(null)
//                .username("unknown")
//                .firstName("Fallback")
//                .lastName("User")
//                .build();
//        return ResponseEntity.ok(
//                ApiResponse.<UserResponse>builder()
//                        .message("Fallback user due to service unavailability")
//                        .payload(fallbackUser)
//                        .status(HttpStatus.NOT_FOUND)
//                        .instant(LocalDateTime.now())
//                        .build()
//        );
//    }
//}
