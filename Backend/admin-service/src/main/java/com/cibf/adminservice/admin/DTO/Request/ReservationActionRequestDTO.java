package com.cibf.adminservice.admin.DTO.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for reservation actions (cancel, update, etc.)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationActionRequestDTO {

    @NotBlank(message = "Action type is required")
    @Size(max = 50, message = "Action type must not exceed 50 characters")
    private String actionType;

    @NotBlank(message = "Reason is required")
    @Size(min = 5, max = 1000, message = "Reason must be between 5 and 1000 characters")
    private String reason;
}

