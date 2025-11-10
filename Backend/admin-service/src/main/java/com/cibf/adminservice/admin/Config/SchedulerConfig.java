package com.cibf.adminservice.admin.Config;

import com.cibf.adminservice.admin.Service.NotificationManagementService;
import com.cibf.adminservice.admin.Service.SystemMonitoringService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class SchedulerConfig {

    private final SystemMonitoringService systemMonitoringService;
    private final NotificationManagementService notificationManagementService;

    // Persist a health snapshot every 10 minutes
    @Scheduled(fixedRate = 600_000)
    public void snapshotHealth() {
        try {
            systemMonitoringService.snapshotSystemHealth();
            log.debug("Health snapshot persisted");
        } catch (Exception e) {
            log.warn("Health snapshot failed: {}", e.getMessage());
        }
    }

    // Process pending notification queue every minute (batch size 50)
    @Scheduled(fixedRate = 60_000)
    public void processNotificationQueue() {
        try {
            int count = notificationManagementService.processPendingQueue(50);
            if (count > 0) {
                log.info("Processed {} queued notifications", count);
            }
        } catch (Exception e) {
            log.warn("Queue processing failed: {}", e.getMessage());
        }
    }
}

