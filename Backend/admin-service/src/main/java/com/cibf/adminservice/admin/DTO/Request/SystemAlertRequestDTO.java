package com.cibf.adminservice.admin.DTO.Request;

import com.cibf.adminservice.admin.Common.AlertSeverity;
import com.cibf.adminservice.admin.Common.AlertType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for creating system alerts
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SystemAlertRequestDTO {

    @NotNull(message = "Alert type is required")
    private AlertType alertType;

    @NotNull(message = "Severity is required")
    private AlertSeverity severity;

    @Size(max = 100, message = "Service name must not exceed 100 characters")
    private String serviceName;

    @NotBlank(message = "Title is required")
    @Size(min = 5, max = 255, message = "Title must be between 5 and 255 characters")
    private String title;

    @NotBlank(message = "Message is required")
    @Size(min = 10, max = 2000, message = "Message must be between 10 and 2000 characters")
    private String message;

    @Size(max = 100, message = "Source must not exceed 100 characters")
    private String source;
}

