package com.cibf.adminservice.admin.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response DTO for admin login
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminLoginResponseDTO {

    private String token;
    private String tokenType = "Bearer";
    private AdminUserResponseDTO admin;
    private String message;

    public AdminLoginResponseDTO(String token, AdminUserResponseDTO admin, String message) {
        this.token = token;
        this.admin = admin;
        this.message = message;
    }
}

