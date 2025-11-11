package com.cibf.adminservice.admin.DTO.Request;

import com.cibf.adminservice.admin.Common.AdminRole;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for updating admin user role
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAdminRoleRequestDTO {

    @NotNull(message = "Role is required")
    private AdminRole role;
}

