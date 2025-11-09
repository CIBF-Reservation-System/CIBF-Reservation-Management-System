package com.cibf.adminservice.admin.Client;

import com.cibf.adminservice.admin.DTO.Internal.NotificationServiceDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Fallback implementation for NotificationServiceClient
 * Provides default responses when notification-service is unavailable
 */
@Slf4j
@Component
public class NotificationServiceClientFallback implements NotificationServiceClient {

    @Override
    public ResponseEntity<Object> sendNotification(NotificationServiceDTO notificationDTO) {
        log.error("Notification Service is unavailable - sendNotification fallback triggered");
        return ResponseEntity.status(503).body("Notification service unavailable");
    }

    @Override
    public ResponseEntity<Object> sendBulkNotifications(List<NotificationServiceDTO> notifications) {
        log.error("Notification Service is unavailable - sendBulkNotifications fallback triggered");
        return ResponseEntity.status(503).body("Notification service unavailable");
    }

    @Override
    public ResponseEntity<List<Object>> getNotificationHistory() {
        log.error("Notification Service is unavailable - getNotificationHistory fallback triggered");
        return ResponseEntity.status(503).body(new ArrayList<>());
    }

    @Override
    public ResponseEntity<Object> getNotificationQueueStatus() {
        log.error("Notification Service is unavailable - getNotificationQueueStatus fallback triggered");
        return ResponseEntity.status(503).body(null);
    }

    @Override
    public ResponseEntity<Object> sendEmailNotification(String email, String subject, String message) {
        log.error("Notification Service is unavailable - sendEmailNotification fallback triggered");
        return ResponseEntity.status(503).body("Notification service unavailable");
    }

    @Override
    public ResponseEntity<Object> sendSmsNotification(String phone, String message) {
        log.error("Notification Service is unavailable - sendSmsNotification fallback triggered");
        return ResponseEntity.status(503).body("Notification service unavailable");
    }

    @Override
    public ResponseEntity<Object> getNotificationStatistics() {
        log.error("Notification Service is unavailable - getNotificationStatistics fallback triggered");
        return ResponseEntity.status(503).body(null);
    }

    @Override
    public ResponseEntity<Object> healthCheck() {
        log.error("Notification Service is unavailable - healthCheck fallback triggered");
        return ResponseEntity.status(503).body(null);
    }
}

