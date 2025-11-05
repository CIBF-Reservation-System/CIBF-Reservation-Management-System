package com.cibf.adminservice.admin.Entity;

import com.cibf.adminservice.admin.Common.ActionStatus;
import com.cibf.adminservice.admin.Common.AlertSeverity;
import com.cibf.adminservice.admin.Common.LogAction;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity class for audit_logs table
 * System-wide audit trail for all admin actions
 */
@Entity
@Table(name = "audit_logs", indexes = {
        @Index(name = "idx_admin_id", columnList = "admin_id"),
        @Index(name = "idx_action_type", columnList = "action_type"),
        @Index(name = "idx_entity_type", columnList = "entity_type"),
        @Index(name = "idx_entity_id", columnList = "entity_id"),
        @Index(name = "idx_created_at", columnList = "created_at"),
        @Index(name = "idx_severity", columnList = "severity"),
        @Index(name = "idx_status", columnList = "status"),
        @Index(name = "idx_combined", columnList = "admin_id, action_type, created_at")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "log_id")
    private Long logId;

    @Column(name = "admin_id", columnDefinition = "BINARY(16)")
    private UUID adminId;

    @Enumerated(EnumType.STRING)
    @Column(name = "action_type", nullable = false)
    private LogAction actionType;

    @Column(name = "entity_type", nullable = false, length = 50)
    private String entityType;

    @Column(name = "entity_id", length = 100)
    private String entityId;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "ip_address", length = 45)
    private String ipAddress;

    @Column(name = "user_agent", length = 255)
    private String userAgent;

    @Column(name = "old_value", columnDefinition = "JSON")
    private String oldValue;

    @Column(name = "new_value", columnDefinition = "JSON")
    private String newValue;

    @Enumerated(EnumType.STRING)
    @Column(name = "severity", nullable = false)
    private AlertSeverity severity = AlertSeverity.LOW;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ActionStatus status = ActionStatus.SUCCESS;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}

