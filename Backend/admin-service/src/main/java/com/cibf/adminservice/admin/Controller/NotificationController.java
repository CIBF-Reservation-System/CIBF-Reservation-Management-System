package com.cibf.adminservice.admin.Controller;

import com.cibf.adminservice.admin.DTO.Internal.NotificationServiceDTO;
import com.cibf.adminservice.admin.DTO.Request.NotificationRequestDTO;
import com.cibf.adminservice.admin.DTO.Response.ApiResponse;
import com.cibf.adminservice.admin.Service.NotificationManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for admin notification dispatch
 * Base URL: /cibf/admin-service/notifications
 */
@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
@Tag(name = "Notification Management", description = "Endpoints for sending and managing notifications")
public class NotificationController {

    private final NotificationManagementService notificationManagementService;

    /**
     * Send single notification
     * POST /cibf/admin-service/notifications/send
     */
    @PostMapping("/send")
    @Operation(summary = "Send notification", description = "Send a single notification to a recipient")
    public ResponseEntity<ApiResponse<String>> sendNotification(
            @Valid @RequestBody NotificationRequestDTO request) {
        log.info("POST /notifications/send - Sending notification of type: {}", request.getNotificationType());

        NotificationServiceDTO notificationDTO = NotificationServiceDTO.builder()
                .notificationType(request.getNotificationType().name())
                .subject(request.getSubject())
                .message(request.getMessage())
                .priority(request.getPriority().name())
                .build();

        notificationManagementService.sendNotification(notificationDTO);
        return ResponseEntity.ok(ApiResponse.success("Notification sent successfully", "Notification dispatched"));
    }

    /**
     * Send bulk notifications
     * POST /cibf/admin-service/notifications/send-bulk
     */
    @PostMapping("/send-bulk")
    @Operation(summary = "Send bulk notifications", description = "Send multiple notifications at once")
    public ResponseEntity<ApiResponse<String>> sendBulkNotifications(
            @Valid @RequestBody List<NotificationRequestDTO> requests) {
        log.info("POST /notifications/send-bulk - Sending {} bulk notifications", requests.size());

        List<NotificationServiceDTO> notifications = requests.stream()
                .map(req -> NotificationServiceDTO.builder()
                        .notificationType(req.getNotificationType().name())
                        .subject(req.getSubject())
                        .message(req.getMessage())
                        .priority(req.getPriority().name())
                        .build())
                .toList();

        notificationManagementService.sendBulkNotifications(notifications);
        return ResponseEntity.ok(ApiResponse.success("Bulk notifications sent successfully",
                requests.size() + " notifications dispatched"));
    }

    /**
     * Send email notification
     * POST /cibf/admin-service/notifications/email
     */
    @PostMapping("/email")
    @Operation(summary = "Send email", description = "Send an email notification")
    public ResponseEntity<ApiResponse<String>> sendEmailNotification(
            @RequestParam @Parameter(description = "Recipient email address") String email,
            @RequestParam @Parameter(description = "Email subject") String subject,
            @RequestParam @Parameter(description = "Email message") String message) {
        log.info("POST /notifications/email - Sending email to: {}", email);
        notificationManagementService.sendEmailNotification(email, subject, message);
        return ResponseEntity.ok(ApiResponse.success("Email sent successfully", "Email dispatched"));
    }

    /**
     * Send SMS notification
     * POST /cibf/admin-service/notifications/sms
     */
    @PostMapping("/sms")
    @Operation(summary = "Send SMS", description = "Send an SMS notification")
    public ResponseEntity<ApiResponse<String>> sendSmsNotification(
            @RequestParam @Parameter(description = "Recipient phone number") String phone,
            @RequestParam @Parameter(description = "SMS message") String message) {
        log.info("POST /notifications/sms - Sending SMS to: {}", phone);
        notificationManagementService.sendSmsNotification(phone, message);
        return ResponseEntity.ok(ApiResponse.success("SMS sent successfully", "SMS dispatched"));
    }

    /**
     * Get notification queue status
     * GET /cibf/admin-service/notifications/queue
     */
    @GetMapping("/queue")
    @Operation(summary = "Get notification queue status", description = "Retrieve current notification queue status")
    public ResponseEntity<ApiResponse<Object>> getNotificationQueueStatus() {
        log.info("GET /notifications/queue - Fetching notification queue status");
        Object queueStatus = notificationManagementService.getNotificationQueueStatus();
        return ResponseEntity.ok(ApiResponse.success("Queue status retrieved successfully", queueStatus));
    }

    /**
     * Get notification history
     * GET /cibf/admin-service/notifications/history
     */
    @GetMapping("/history")
    @Operation(summary = "Get notification history", description = "Retrieve notification history")
    public ResponseEntity<ApiResponse<List<Object>>> getNotificationHistory() {
        log.info("GET /notifications/history - Fetching notification history");
        List<Object> history = notificationManagementService.getNotificationHistory();
        return ResponseEntity.ok(ApiResponse.success("Notification history retrieved successfully", history));
    }

    /**
     * Get notification statistics
     * GET /cibf/admin-service/notifications/statistics
     */
    @GetMapping("/statistics")
    @Operation(summary = "Get notification statistics", description = "Retrieve notification statistics and metrics")
    public ResponseEntity<ApiResponse<Object>> getNotificationStatistics() {
        log.info("GET /notifications/statistics - Fetching notification statistics");
        Object statistics = notificationManagementService.getNotificationStatistics();
        return ResponseEntity.ok(ApiResponse.success("Notification statistics retrieved successfully", statistics));
    }
}

