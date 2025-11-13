package com.cibf.notificationservice.notification.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.cibf.notificationservice.notification.model.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationResponse {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("recipient_email")
    private String recipientEmail;

    @JsonProperty("recipient_name")
    private String recipientName;

    @JsonProperty("notification_type")
    private NotificationType notificationType;

    @JsonProperty("subject")
    private String subject;

    @JsonProperty("status")
    private String status;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("sent_at")
    private LocalDateTime sentAt;

    @JsonProperty("reference_id")
    private String referenceId;

    @JsonProperty("message")
    private String message;

    @JsonProperty("error_message")
    private String errorMessage;

    @JsonProperty("is_sent")
    public boolean isSent() {
        return "SENT".equals(status);
    }

    @JsonProperty("is_failed")
    public boolean isFailed() {
        return "FAILED".equals(status);
    }

    @JsonProperty("is_pending")
    public boolean isPending() {
        return "PENDING".equals(status);
    }

    @JsonProperty("processing_time_seconds")
    public Long getProcessingTimeSeconds() {
        if (createdAt != null && sentAt != null) {
            return java.time.Duration.between(createdAt, sentAt).getSeconds();
        }
        return null;
    }
}