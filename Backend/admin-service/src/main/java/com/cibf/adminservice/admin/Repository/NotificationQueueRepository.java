package com.cibf.adminservice.admin.Repository;

import com.cibf.adminservice.admin.Common.NotificationPriority;
import com.cibf.adminservice.admin.Common.NotificationStatus;
import com.cibf.adminservice.admin.Common.NotificationType;
import com.cibf.adminservice.admin.Common.RecipientType;
import com.cibf.adminservice.admin.Entity.NotificationQueue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Repository interface for NotificationQueue entity
 */
@Repository
public interface NotificationQueueRepository extends JpaRepository<NotificationQueue, Long> {

    Page<NotificationQueue> findByStatus(NotificationStatus status, Pageable pageable);

    List<NotificationQueue> findByStatusAndScheduledAtBefore(NotificationStatus status, LocalDateTime dateTime);

    Page<NotificationQueue> findByRecipientId(UUID recipientId, Pageable pageable);

    Page<NotificationQueue> findByNotificationType(NotificationType notificationType, Pageable pageable);

    Page<NotificationQueue> findByPriority(NotificationPriority priority, Pageable pageable);

    Page<NotificationQueue> findByRecipientType(RecipientType recipientType, Pageable pageable);

    long countByStatus(NotificationStatus status);

    List<NotificationQueue> findByStatusAndRetryCountLessThanEqual(NotificationStatus status, Integer maxRetries);
}

