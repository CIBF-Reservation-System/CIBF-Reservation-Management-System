package com.cibf.adminservice.admin.DTO.Response;

import com.cibf.adminservice.admin.Common.NotificationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Response DTO for notification status
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponseDTO {

    private Long notificationId;
    private String recipientType;
    private String notificationType;
    private String subject;
    private NotificationStatus status;
    private LocalDateTime sentAt;
    private String errorMessage;
}

