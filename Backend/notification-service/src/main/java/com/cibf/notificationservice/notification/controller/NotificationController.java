package com.cibf.notificationservice.notification.controller;

import com.cibf.notificationservice.notification.model.dto.NotificationResponse;
import com.cibf.notificationservice.notification.model.entity.NotificationLog;
import com.cibf.notificationservice.notification.model.enums.NotificationType;
import com.cibf.notificationservice.notification.repository.NotificationLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class NotificationController {

    private final NotificationLogRepository repository;


    @GetMapping
    public ResponseEntity<List<NotificationLog>> getAllNotifications() {
        log.info(" Getting all notifications");

        List<NotificationLog> notifications = repository.findAll();

        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getNotificationById(@PathVariable Long id) {
        log.info(" Getting notification with ID: {}", id);

        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @GetMapping("/email/{email}")
    public ResponseEntity<List<NotificationLog>> getNotificationsByEmail(
            @PathVariable String email
    ) {
        log.info(" Getting notifications for email: {}", email);

        List<NotificationLog> notifications =
                repository.findByRecipientEmailOrderByCreatedAtDesc(email);

        return ResponseEntity.ok(notifications);
    }


    @GetMapping("/type/{type}")
    public ResponseEntity<List<NotificationLog>> getNotificationsByType(
            @PathVariable NotificationType type
    ) {
        log.info(" Getting notifications of type: {}", type);

        List<NotificationLog> notifications = repository.findByNotificationType(type);

        return ResponseEntity.ok(notifications);
    }


    @GetMapping("/status/{status}")
    public ResponseEntity<List<NotificationLog>> getNotificationsByStatus(
            @PathVariable String status
    ) {
        log.info(" Getting notifications with status: {}", status);

        List<NotificationLog> notifications = repository.findByStatus(status);

        return ResponseEntity.ok(notifications);
    }


    @GetMapping("/stats")
    public ResponseEntity<Map<String, Long>> getNotificationStats() {
        log.info(" Getting notification statistics");

        long total = repository.count();
        long sent = repository.countByStatus("SENT");
        long failed = repository.countByStatus("FAILED");
        long pending = repository.countByStatus("PENDING");
        long reservations = repository.countByNotificationType(
                NotificationType.RESERVATION_CONFIRMATION
        );
        long registrations = repository.countByNotificationType(
                NotificationType.REGISTRATION_CONFIRMATION
        );

        Map<String, Long> stats = Map.of(
                "total", total,
                "sent", sent,
                "failed", failed,
                "pending", pending,
                "reservations", reservations,
                "registrations", registrations
        );

        return ResponseEntity.ok(stats);
    }


    @GetMapping("/recent")
    public ResponseEntity<List<NotificationLog>> getRecentNotifications(
            @RequestParam(defaultValue = "10") int limit
    ) {
        log.info(" Getting {} recent notifications", limit);

        List<NotificationLog> notifications = repository.findTop10ByOrderByCreatedAtDesc();

        return ResponseEntity.ok(notifications);
    }


    @GetMapping("/date-range")
    public ResponseEntity<List<NotificationLog>> getNotificationsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end
    ) {
        log.info(" Getting notifications between {} and {}", start, end);

        List<NotificationLog> notifications =
                repository.findByCreatedAtBetween(start, end);

        return ResponseEntity.ok(notifications);
    }
}