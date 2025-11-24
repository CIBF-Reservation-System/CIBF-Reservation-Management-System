package com.cibf.adminservice.admin.DTO.Internal;

// Removed Lombok annotations

/**
 * Internal DTO for notification service communication
 */
public class NotificationServiceDTO {
    private String recipientEmail;
    private String recipientPhone;
    private String notificationType;
    private String subject;
    private String message;
    private String priority;

    public NotificationServiceDTO() {}

    public String getRecipientEmail() { return recipientEmail; }
    public void setRecipientEmail(String recipientEmail) { this.recipientEmail = recipientEmail; }

    public String getRecipientPhone() { return recipientPhone; }
    public void setRecipientPhone(String recipientPhone) { this.recipientPhone = recipientPhone; }

    public String getNotificationType() { return notificationType; }
    public void setNotificationType(String notificationType) { this.notificationType = notificationType; }

    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
}

