package com.cibf.adminservice.admin.DTO.Internal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Internal DTO for notification service communication
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationServiceDTO {

    private String recipientEmail;
    private String recipientPhone;
    private String notificationType;
    private String subject;
    private String message;
    private String priority;
}

