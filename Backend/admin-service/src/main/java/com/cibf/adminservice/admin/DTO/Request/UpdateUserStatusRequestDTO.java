package com.cibf.adminservice.admin.DTO.Request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for updating user status (enable/disable)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserStatusRequestDTO {

    @NotNull(message = "Active status is required")
    private Boolean isActive;

    @Size(max = 500, message = "Reason must not exceed 500 characters")
    private String reason;
}

