package com.cibf.adminservice.admin.Service;

import com.cibf.adminservice.admin.Client.NotificationServiceClient;
import com.cibf.adminservice.admin.Common.NotificationStatus;
import com.cibf.adminservice.admin.Entity.NotificationQueue;
import com.cibf.adminservice.admin.DTO.Internal.NotificationServiceDTO;
import com.cibf.adminservice.admin.Repository.NotificationQueueRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Service for sending notifications via notification-service
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationManagementService {

    private final NotificationServiceClient notificationServiceClient;
    private final NotificationQueueRepository notificationQueueRepository;

    /**
     * Send single notification
     */
    public void sendNotification(NotificationServiceDTO notificationDTO) {
        log.info("Sending notification to: {}", notificationDTO.getRecipientEmail());
        try {
            ResponseEntity<Object> response = notificationServiceClient.sendNotification(notificationDTO);
            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Successfully sent notification");
            } else {
                log.warn("Notification sent with non-2xx status: {}", response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("Error sending notification: {}", e.getMessage());
            // Don't throw exception - notifications are not critical
        }
    }

    /**
     * Send bulk notifications
     */
    public void sendBulkNotifications(List<NotificationServiceDTO> notifications) {
        log.info("Sending {} bulk notifications", notifications.size());
        try {
            ResponseEntity<Object> response = notificationServiceClient.sendBulkNotifications(notifications);
            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Successfully sent bulk notifications");
            } else {
                log.warn("Bulk notifications sent with non-2xx status: {}", response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("Error sending bulk notifications: {}", e.getMessage());
            // Don't throw exception - notifications are not critical
        }
    }

    /**
     * Send email notification
     */
    public void sendEmailNotification(String email, String subject, String message) {
        log.info("Sending email to: {}", email);
        try {
            ResponseEntity<Object> response = notificationServiceClient.sendEmailNotification(email, subject, message);
            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Successfully sent email notification");
            } else {
                log.warn("Email sent with non-2xx status: {}", response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("Error sending email: {}", e.getMessage());
        }
    }

    /**
     * Send SMS notification
     */
    public void sendSmsNotification(String phone, String message) {
        log.info("Sending SMS to: {}", phone);
        try {
            ResponseEntity<Object> response = notificationServiceClient.sendSmsNotification(phone, message);
            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Successfully sent SMS notification");
            } else {
                log.warn("SMS sent with non-2xx status: {}", response.getStatusCode());
            }
        } catch (Exception e) {
            log.error("Error sending SMS: {}", e.getMessage());
        }
    }

    /**
     * Get notification history
     */
    public List<Object> getNotificationHistory() {
        log.info("Fetching notification history");
        try {
            ResponseEntity<List<Object>> response = notificationServiceClient.getNotificationHistory();
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                log.info("Successfully fetched notification history");
                return response.getBody();
            }
            return List.of();
        } catch (Exception e) {
            log.error("Error fetching notification history: {}", e.getMessage());
            return List.of();
        }
    }

    /**
     * Get notification statistics
     */
    public Object getNotificationStatistics() {
        log.info("Fetching notification statistics");
        try {
            ResponseEntity<Object> response = notificationServiceClient.getNotificationStatistics();
            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Successfully fetched notification statistics");
                return response.getBody();
            }
            return null;
        } catch (Exception e) {
            log.error("Error fetching notification statistics: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Get notification queue status
     */
    public Object getNotificationQueueStatus() {
        log.info("Fetching notification queue status");
        try {
            ResponseEntity<Object> response = notificationServiceClient.getNotificationQueueStatus();
            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Successfully fetched notification queue status");
                return response.getBody();
            }
            return null;
        } catch (Exception e) {
            log.error("Error fetching notification queue status: {}", e.getMessage());
            return null;
        }
    }

    public NotificationQueue enqueueEmail(String email, String subject, String message, UUID createdBy) {
        NotificationQueue nq = new NotificationQueue();
        nq.setNotificationType(com.cibf.adminservice.admin.Common.NotificationType.EMAIL);
        nq.setRecipientType(com.cibf.adminservice.admin.Common.RecipientType.USER);
        nq.setRecipientEmail(email);
        nq.setSubject(subject);
        nq.setMessage(message);
        nq.setPriority(com.cibf.adminservice.admin.Common.NotificationPriority.MEDIUM);
        nq.setStatus(NotificationStatus.PENDING);
        nq.setScheduledAt(LocalDateTime.now());
        nq.setCreatedBy(createdBy);
        return notificationQueueRepository.save(nq);
    }

    public int processPendingQueue(int batchSize) {
        var pending = notificationQueueRepository.findByStatusAndScheduledAtBefore(NotificationStatus.PENDING, LocalDateTime.now());
        int processed = 0;
        for (NotificationQueue item : pending.stream().limit(batchSize).toList()) {
            try {
                sendEmailNotification(item.getRecipientEmail(), item.getSubject(), item.getMessage());
                item.setStatus(NotificationStatus.SENT);
                item.setSentAt(LocalDateTime.now());
                item.setErrorMessage(null);
                notificationQueueRepository.save(item);
                processed++;
            } catch (Exception e) {
                item.setRetryCount(item.getRetryCount() + 1);
                item.setErrorMessage(e.getMessage());
                if (item.getRetryCount() >= item.getMaxRetries()) {
                    item.setStatus(NotificationStatus.FAILED);
                } else {
                    item.setStatus(NotificationStatus.PENDING); // will retry later
                }
                notificationQueueRepository.save(item);
            }
        }
        return processed;
    }

    public Object getQueueStatusSummary() {
        long pending = notificationQueueRepository.countByStatus(NotificationStatus.PENDING);
        long sent = notificationQueueRepository.countByStatus(NotificationStatus.SENT);
        long failed = notificationQueueRepository.countByStatus(NotificationStatus.FAILED);
        return java.util.Map.of(
                "pending", pending,
                "sent", sent,
                "failed", failed
        );
    }

    public List<NotificationQueue> getQueuePage(int page, int size) {
        return notificationQueueRepository.findAll(PageRequest.of(page, size)).getContent();
    }
}
