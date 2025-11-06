    private UUID adminId;
    private String username;
    private String email;
    private String fullName;
    private String role;
    private String token;
    private String tokenType = "Bearer";
}
package com.cibf.adminservice.admin.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Response DTO for admin login
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminLoginResponseDTO {


