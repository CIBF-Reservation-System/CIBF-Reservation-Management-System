package com.cibf.adminservice.admin.Entity;

import com.cibf.adminservice.admin.Common.AlertSeverity;
import com.cibf.adminservice.admin.Common.AlertStatus;
import com.cibf.adminservice.admin.Common.AlertType;
import com.cibf.adminservice.admin.Common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity class for system_alerts table
 * System health alerts and notifications
 */
@Entity
@Table(name = "system_alerts", indexes = {
        @Index(name = "idx_alert_type", columnList = "alert_type"),
        @Index(name = "idx_severity", columnList = "severity"),
        @Index(name = "idx_status", columnList = "status"),
        @Index(name = "idx_created_at", columnList = "created_at"),
        @Index(name = "idx_service_name", columnList = "service_name"),
        @Index(name = "idx_combined", columnList = "status, severity, created_at")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SystemAlert extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alert_id")
    private Long alertId;

    @Enumerated(EnumType.STRING)
    @Column(name = "alert_type", nullable = false)
    private AlertType alertType;

    @Enumerated(EnumType.STRING)
    @Column(name = "severity", nullable = false)
    private AlertSeverity severity = AlertSeverity.MEDIUM;

    @Column(name = "service_name", length = 100)
    private String serviceName;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "message", nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(name = "source", length = 100)
    private String source;

    @Column(name = "metadata", columnDefinition = "JSON")
    private String metadata;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AlertStatus status = AlertStatus.OPEN;

    @Column(name = "acknowledged_by", columnDefinition = "BINARY(16)")
    private UUID acknowledgedBy;

    @Column(name = "acknowledged_at")
    private LocalDateTime acknowledgedAt;

    @Column(name = "resolved_by", columnDefinition = "BINARY(16)")
    private UUID resolvedBy;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    @Column(name = "resolution_notes", columnDefinition = "TEXT")
    private String resolutionNotes;
}

