package com.cibf.adminservice.admin.Entity;

import com.cibf.adminservice.admin.Common.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity class for notification_queue table
 * Queue for pending admin notifications
 */
@Entity
@Table(name = "notification_queue", indexes = {
        @Index(name = "idx_status", columnList = "status"),
        @Index(name = "idx_notification_type", columnList = "notification_type"),
        @Index(name = "idx_recipient_id", columnList = "recipient_id"),
        @Index(name = "idx_priority", columnList = "priority"),
        @Index(name = "idx_scheduled_at", columnList = "scheduled_at"),
        @Index(name = "idx_created_at", columnList = "created_at"),
        @Index(name = "idx_combined", columnList = "status, priority, scheduled_at")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class NotificationQueue extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "queue_id")
    private Long queueId;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_type", nullable = false)
    private NotificationType notificationType;

    @Enumerated(EnumType.STRING)
    @Column(name = "recipient_type", nullable = false)
    private RecipientType recipientType;

    @Column(name = "recipient_id", columnDefinition = "BINARY(16)")
    private UUID recipientId;

    @Column(name = "recipient_email", length = 100)
    private String recipientEmail;

    @Column(name = "recipient_phone", length = 20)
    private String recipientPhone;

    @Column(name = "subject", nullable = false, length = 255)
    private String subject;

    @Column(name = "message", nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(name = "template_name", length = 100)
    private String templateName;

    @Column(name = "template_variables", columnDefinition = "JSON")
    private String templateVariables;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false)
    private NotificationPriority priority = NotificationPriority.MEDIUM;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private NotificationStatus status = NotificationStatus.PENDING;

    @Column(name = "scheduled_at")
    private LocalDateTime scheduledAt;

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    @Column(name = "retry_count", nullable = false)
    private Integer retryCount = 0;

    @Column(name = "max_retries", nullable = false)
    private Integer maxRetries = 3;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "created_by", columnDefinition = "BINARY(16)")
    private UUID createdBy;
}

