package com.cibf.adminservice.admin.DTO.Response;

import com.cibf.adminservice.admin.Common.AdminRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO for admin user data
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminUserResponseDTO {

    private UUID adminId;
    private String username;
    private String email;
    private String fullName;
    private String phone;
    private AdminRole role;
    private Boolean isActive;
    private LocalDateTime lastLogin;
    private Integer failedLoginAttempts;
    private LocalDateTime passwordChangedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

