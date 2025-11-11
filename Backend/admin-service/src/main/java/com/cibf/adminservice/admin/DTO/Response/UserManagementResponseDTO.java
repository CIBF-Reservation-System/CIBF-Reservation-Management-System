package com.cibf.adminservice.admin.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO for user management (from user-service cache)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserManagementResponseDTO {

    private UUID userId;
    private String username;
    private String email;
    private String fullName;
    private String phone;
    private String role;
    private Boolean isActive;
    private Boolean emailVerified;
    private LocalDateTime lastLogin;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

