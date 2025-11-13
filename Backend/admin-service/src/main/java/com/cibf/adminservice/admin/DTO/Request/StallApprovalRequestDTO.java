package com.cibf.adminservice.admin.DTO.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for stall approval/rejection
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StallApprovalRequestDTO {

    @NotNull(message = "Approval status is required")
    private Boolean approved;

    @NotBlank(message = "Remarks are required")
    @Size(min = 5, max = 1000, message = "Remarks must be between 5 and 1000 characters")
    private String remarks;
}

