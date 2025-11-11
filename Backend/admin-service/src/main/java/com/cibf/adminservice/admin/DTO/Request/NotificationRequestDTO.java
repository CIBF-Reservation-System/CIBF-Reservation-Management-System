package com.cibf.adminservice.admin.DTO.Request;

import com.cibf.adminservice.admin.Common.NotificationPriority;
import com.cibf.adminservice.admin.Common.NotificationType;
import com.cibf.adminservice.admin.Common.RecipientType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Request DTO for sending notifications
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequestDTO {

    @NotNull(message = "Recipient type is required")
    private RecipientType recipientType;

    private List<String> recipientIds;

    @NotNull(message = "Notification type is required")
    private NotificationType notificationType;

    @NotBlank(message = "Subject is required")
    @Size(min = 3, max = 255, message = "Subject must be between 3 and 255 characters")
    private String subject;

    @NotBlank(message = "Message is required")
    @Size(min = 10, max = 5000, message = "Message must be between 10 and 5000 characters")
    private String message;

    @NotNull(message = "Priority is required")
    private NotificationPriority priority;
}

