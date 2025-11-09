package com.cibf.adminservice.admin.Client;

import com.cibf.adminservice.admin.DTO.Internal.NotificationServiceDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Feign Client for Notification Service
 * Communicates with notification-service to send notifications
 */
@FeignClient(
        name = "notification-service",
        url = "${spring.cloud.openfeign.client.config.notification-service.url}",
        fallback = NotificationServiceClientFallback.class
)
public interface NotificationServiceClient {

    /**
     * Send single notification
     */
    @PostMapping("/api/v1/notifications/send")
    ResponseEntity<Object> sendNotification(@RequestBody NotificationServiceDTO notificationDTO);

    /**
     * Send bulk notifications
     */
    @PostMapping("/api/v1/notifications/send-bulk")
    ResponseEntity<Object> sendBulkNotifications(@RequestBody List<NotificationServiceDTO> notifications);

    /**
     * Get notification history
     */
    @GetMapping("/api/v1/notifications/history")
    ResponseEntity<List<Object>> getNotificationHistory();

    /**
     * Get notification queue status
     */
    @GetMapping("/api/v1/notifications/queue")
    ResponseEntity<Object> getNotificationQueueStatus();

    /**
     * Send email notification
     */
    @PostMapping("/api/v1/notifications/email")
    ResponseEntity<Object> sendEmailNotification(
            @RequestParam("email") String email,
            @RequestParam("subject") String subject,
            @RequestParam("message") String message
    );

    /**
     * Send SMS notification
     */
    @PostMapping("/api/v1/notifications/sms")
    ResponseEntity<Object> sendSmsNotification(
            @RequestParam("phone") String phone,
            @RequestParam("message") String message
    );

    /**
     * Get notification statistics
     */
    @GetMapping("/api/v1/notifications/statistics")
    ResponseEntity<Object> getNotificationStatistics();
}

