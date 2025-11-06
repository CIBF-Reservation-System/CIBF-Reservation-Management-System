    private UUID acknowledgedBy;
    private String acknowledgedByName;
    private LocalDateTime acknowledgedAt;
    private UUID resolvedBy;
    private String resolvedByName;
    private LocalDateTime resolvedAt;
    private String resolutionNotes;
    private LocalDateTime createdAt;
}
package com.cibf.adminservice.admin.DTO.Response;

import com.cibf.adminservice.admin.Common.AlertSeverity;
import com.cibf.adminservice.admin.Common.AlertStatus;
import com.cibf.adminservice.admin.Common.AlertType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO for system alerts
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SystemAlertResponseDTO {

    private Long alertId;
    private AlertType alertType;
    private AlertSeverity severity;
    private String serviceName;
    private String title;
    private String message;
    private String source;
    private AlertStatus status;

