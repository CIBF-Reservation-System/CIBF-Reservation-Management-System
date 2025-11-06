package com.cibf.adminservice.admin.DTO.Response;

import com.cibf.adminservice.admin.Common.ActionStatus;
import com.cibf.adminservice.admin.Common.AlertSeverity;
import com.cibf.adminservice.admin.Common.LogAction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO for audit log data
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditLogResponseDTO {

    private Long logId;
    private UUID adminId;
    private String adminUsername;
    private LogAction actionType;
    private String entityType;
    private String entityId;
    private String description;
    private String ipAddress;
    private AlertSeverity severity;
    private ActionStatus status;
    private String errorMessage;
    private LocalDateTime createdAt;
}

