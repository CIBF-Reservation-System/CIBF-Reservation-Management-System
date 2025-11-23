package com.cibf.notificationservice.notification.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import com.cibf.notificationservice.notification.model.enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationRequest {

    @NotBlank(message = "Recipient email is required")
    @Email(message = "Invalid email format")
    @JsonProperty("recipient_email")
    private String recipientEmail;

    @NotBlank(message = "Recipient name is required")
    @JsonProperty("recipient_name")
    private String recipientName;

    @NotNull(message = "Notification type is required")
    @JsonProperty("notification_type")
    private NotificationType notificationType;

    @NotBlank(message = "Subject is required")
    @JsonProperty("subject")
    private String subject;

    @JsonProperty("message")
    private String message;

    @JsonProperty("reference_id")
    private String referenceId;

    @JsonProperty("template_name")
    private String templateName;

    @JsonProperty("template_data")
    private Map<String, Object> templateData;

    @JsonProperty("send_immediately")
    @Builder.Default
    private Boolean sendImmediately = true;

    @JsonProperty("priority")
    @Builder.Default
    private String priority = "NORMAL";

    public boolean hasTemplate() {
        return templateName != null && !templateName.isEmpty();
    }

    public boolean hasMessage() {
        return message != null && !message.isEmpty();
    }

    public Object getTemplateValue(String key) {
        if (templateData == null) {
            return null;
        }
        return templateData.get(key);
    }

    public boolean isHighPriority() {
        return "HIGH".equalsIgnoreCase(priority);
    }
}