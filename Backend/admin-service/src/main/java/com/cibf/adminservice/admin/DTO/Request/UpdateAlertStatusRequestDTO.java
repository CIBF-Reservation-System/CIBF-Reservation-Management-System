package com.cibf.adminservice.admin.DTO.Request;

import com.cibf.adminservice.admin.Common.AlertStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Request DTO for updating alert status
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAlertStatusRequestDTO {

    private AlertStatus status;

    @NotNull(message = "Resolved by is required")
    private UUID resolvedBy;

    @Size(max = 1000, message = "Resolution notes must not exceed 1000 characters")
    private String resolutionNotes;
}

