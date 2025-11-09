package com.cibf.adminservice.admin.Client;

import com.cibf.adminservice.admin.DTO.Internal.UserServiceDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Feign Client for User Service
 * Communicates with user-service to fetch user data
 */
@FeignClient(
        name = "user-service",
        url = "${spring.cloud.openfeign.client.config.user-service.url}",
        fallback = UserServiceClientFallback.class
)
public interface UserServiceClient {

    /**
     * Get all users from user service
     */
    @GetMapping("/api/v1/users")
    ResponseEntity<List<UserServiceDTO>> getAllUsers();

    /**
     * Get user by ID
     */
    @GetMapping("/api/v1/users/{userId}")
    ResponseEntity<UserServiceDTO> getUserById(@PathVariable("userId") UUID userId);

    /**
     * Get current user profile
     */
    @GetMapping("/api/v1/auth/profile")
    ResponseEntity<UserServiceDTO> getCurrentUserProfile(@RequestHeader("Authorization") String token);

    /**
     * Update user status (enable/disable)
     */
    @PatchMapping("/api/v1/users/{userId}/status")
    ResponseEntity<UserServiceDTO> updateUserStatus(
            @PathVariable("userId") UUID userId,
            @RequestParam("isActive") Boolean isActive
    );

    /**
     * Update user role
     */
    @PatchMapping("/api/v1/users/{userId}/role")
    ResponseEntity<UserServiceDTO> updateUserRole(
            @PathVariable("userId") UUID userId,
            @RequestParam("role") String role
    );

    /**
     * Search users by username or email
     */
    @GetMapping("/api/v1/users/search")
    ResponseEntity<List<UserServiceDTO>> searchUsers(@RequestParam("query") String query);

    /**
     * Get user statistics
     */
    @GetMapping("/api/v1/users/statistics")
    ResponseEntity<Object> getUserStatistics();

    /**
     * Health check endpoint
     */
    @GetMapping("/actuator/health")
    ResponseEntity<Object> healthCheck();
}

