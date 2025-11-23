package com.cibf.notificationservice.notification.repository;

import com.cibf.notificationservice.notification.model.entity.NotificationLog;
import com.cibf.notificationservice.notification.model.enums.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface NotificationLogRepository extends JpaRepository<NotificationLog, Long> {


    List<NotificationLog> findByRecipientEmail(String email);


    List<NotificationLog> findByNotificationType(NotificationType type);


    List<NotificationLog> findByStatus(String status);


    List<NotificationLog> findByReferenceId(String referenceId);


    Optional<NotificationLog> findFirstByReferenceId(String referenceId);


    List<NotificationLog> findByRecipientEmailAndStatus(String email, String status);


    List<NotificationLog> findByNotificationTypeAndStatus(
            NotificationType type,
            String status
    );


    List<NotificationLog> findByCreatedAtBetween(
            LocalDateTime start,
            LocalDateTime end
    );


    List<NotificationLog> findBySentAtAfter(LocalDateTime date);

    List<NotificationLog> findByRecipientEmailOrderByCreatedAtDesc(String email);

    List<NotificationLog> findTop10ByOrderByCreatedAtDesc();

    long countByStatus(String status);

    long countByNotificationType(NotificationType type);

    boolean existsByReferenceId(String referenceId);

    @Query("SELECT n FROM NotificationLog n WHERE n.recipientEmail = :email AND n.status = 'SENT'")
    List<NotificationLog> findSentNotificationsByEmail(@Param("email") String email);

    @Query(
            value = "SELECT * FROM notification_logs WHERE created_at > DATE_SUB(NOW(), INTERVAL 7 DAY)",
            nativeQuery = true
    )
    List<NotificationLog> findNotificationsFromLastWeek();

    @Query(
            "SELECT n.notificationType, COUNT(n) FROM NotificationLog n " +
                    "WHERE n.status = 'FAILED' " +
                    "GROUP BY n.notificationType"
    )
    List<Object[]> countFailedNotificationsByType();
}
