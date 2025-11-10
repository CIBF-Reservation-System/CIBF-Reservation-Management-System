package com.cibf.adminservice.admin.Service;

import com.cibf.adminservice.admin.Client.NotificationServiceClient;
import com.cibf.adminservice.admin.Client.ReservationServiceClient;
import com.cibf.adminservice.admin.Client.StallServiceClient;
import com.cibf.adminservice.admin.Client.UserServiceClient;
import com.cibf.adminservice.admin.Common.AlertSeverity;
import com.cibf.adminservice.admin.Common.AlertStatus;
import com.cibf.adminservice.admin.Common.AlertType;
import com.cibf.adminservice.admin.DTO.Response.SystemHealthResponseDTO;
import com.cibf.adminservice.admin.Entity.SystemAlert;
import com.cibf.adminservice.admin.Entity.SystemHealthMetric;
import com.cibf.adminservice.admin.Repository.SystemAlertRepository;
import com.cibf.adminservice.admin.Repository.SystemHealthMetricRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Service for monitoring system health and generating alerts
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class SystemMonitoringService {

    private final UserServiceClient userServiceClient;
    private final StallServiceClient stallServiceClient;
    private final ReservationServiceClient reservationServiceClient;
    private final NotificationServiceClient notificationServiceClient;
    private final SystemHealthMetricRepository healthMetricRepository;
    private final SystemAlertRepository systemAlertRepository;
    private final DataSource dataSource;

    /**
     * Get overall system health status
     */
    public SystemHealthResponseDTO getSystemHealth() {
        log.info("Checking overall system health");

        Map<String, SystemHealthResponseDTO.ServiceHealth> services = new HashMap<>();

        // Check all microservices
        services.put("user-service", checkServiceHealth("user-service", this::checkUserService));
        services.put("stall-service", checkServiceHealth("stall-service", this::checkStallService));
        services.put("reservation-service", checkServiceHealth("reservation-service", this::checkReservationService));
        services.put("notification-service", checkServiceHealth("notification-service", this::checkNotificationService));
        services.put("admin-service-database", checkDatabaseHealth());

        // Calculate overall status
        boolean allHealthy = services.values().stream()
                .allMatch(s -> "HEALTHY".equals(s.getStatus()));

        long unhealthyCount = services.values().stream()
                .filter(s -> !"HEALTHY".equals(s.getStatus()))
                .count();

        String overallStatus = allHealthy ? "HEALTHY" : (unhealthyCount >= 3 ? "CRITICAL" : "DEGRADED");

        return SystemHealthResponseDTO.builder()
                .overallStatus(overallStatus)
                .timestamp(LocalDateTime.now())
                .services(services)
                .healthyServices(services.values().stream().filter(s -> "HEALTHY".equals(s.getStatus())).count())
                .totalServices((long) services.size())
                .build();
    }

    /**
     * Check health of individual services
     */
    private SystemHealthResponseDTO.ServiceHealth checkServiceHealth(String serviceName, ServiceChecker checker) {
        try {
            long startTime = System.currentTimeMillis();
            boolean isHealthy = checker.check();
            long responseTime = System.currentTimeMillis() - startTime;

            return SystemHealthResponseDTO.ServiceHealth.builder()
                    .status(isHealthy ? "HEALTHY" : "UNHEALTHY")
                    .responseTime(responseTime)
                    .message(isHealthy ? "Service is operational" : "Service is not responding")
                    .lastChecked(LocalDateTime.now())
                    .build();
        } catch (Exception e) {
            log.error("Error checking {}: {}", serviceName, e.getMessage());
            return SystemHealthResponseDTO.ServiceHealth.builder()
                    .status("UNHEALTHY")
                    .responseTime(-1L)
                    .message("Error: " + e.getMessage())
                    .lastChecked(LocalDateTime.now())
                    .build();
        }
    }

    /**
     * Check database health
     */
    private SystemHealthResponseDTO.ServiceHealth checkDatabaseHealth() {
        try {
            long startTime = System.currentTimeMillis();
            try (Connection connection = dataSource.getConnection()) {
                boolean isValid = connection.isValid(5);
                long responseTime = System.currentTimeMillis() - startTime;

                return SystemHealthResponseDTO.ServiceHealth.builder()
                        .status(isValid ? "HEALTHY" : "UNHEALTHY")
                        .responseTime(responseTime)
                        .message(isValid ? "Database connection is healthy" : "Database connection is invalid")
                        .lastChecked(LocalDateTime.now())
                        .build();
            }
        } catch (Exception e) {
            log.error("Database health check failed: {}", e.getMessage());
            return SystemHealthResponseDTO.ServiceHealth.builder()
                    .status("UNHEALTHY")
                    .responseTime(-1L)
                    .message("Database error: " + e.getMessage())
                    .lastChecked(LocalDateTime.now())
                    .build();
        }
    }

    /**
     * Check user service health
     */
    private boolean checkUserService() {
        try {
            ResponseEntity<?> response = userServiceClient.healthCheck();
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check stall service health
     */
    private boolean checkStallService() {
        try {
            ResponseEntity<?> response = stallServiceClient.healthCheck();
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check reservation service health
     */
    private boolean checkReservationService() {
        try {
            ResponseEntity<?> response = reservationServiceClient.healthCheck();
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Check notification service health
     */
    private boolean checkNotificationService() {
        try {
            ResponseEntity<?> response = notificationServiceClient.healthCheck();
            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Generate system alert
     */
    public SystemAlert generateAlert(AlertType alertType, AlertSeverity severity,
                                     String serviceName, String title, String message) {
        log.info("Generating system alert: {} - {}", alertType, title);

        SystemAlert alert = SystemAlert.builder()
                .alertType(alertType)
                .severity(severity)
                .serviceName(serviceName)
                .title(title)
                .message(message)
                .source("SYSTEM_MONITOR")
                .status(AlertStatus.OPEN)
                .build();

        SystemAlert savedAlert = systemAlertRepository.save(alert);
        log.info("System alert created with ID: {}", savedAlert.getAlertId());

        return savedAlert;
    }

    /**
     * Get all system alerts
     */
    public List<SystemAlert> getAllAlerts() {
        log.info("Fetching all system alerts");
        return systemAlertRepository.findAll();
    }

    /**
     * Get alerts by severity
     */
    public List<SystemAlert> getAlertsBySeverity(AlertSeverity severity) {
        log.info("Fetching alerts with severity: {}", severity);
        return systemAlertRepository.findBySeverityOrderByCreatedAtDesc(severity);
    }

    /**
     * Get alerts by status
     */
    public List<SystemAlert> getAlertsByStatus(AlertStatus status) {
        log.info("Fetching alerts with status: {}", status);
        return systemAlertRepository.findByStatusOrderByCreatedAtDesc(status);
    }

    /**
     * Get recent alerts
     */
    public List<SystemAlert> getRecentAlerts(int limit) {
        log.info("Fetching {} most recent alerts", limit);
        List<SystemAlert> allAlerts = systemAlertRepository.findAllByOrderByCreatedAtDesc();
        return allAlerts.stream().limit(limit).toList();
    }

    /**
     * Acknowledge alert
     */
    public SystemAlert acknowledgeAlert(Long alertId, UUID acknowledgedBy) {
        log.info("Acknowledging alert: {} by user: {}", alertId, acknowledgedBy);

        SystemAlert alert = systemAlertRepository.findById(alertId)
                .orElseThrow(() -> new RuntimeException("Alert not found"));

        alert.setStatus(AlertStatus.ACKNOWLEDGED);
        alert.setAcknowledgedBy(acknowledgedBy);
        alert.setAcknowledgedAt(LocalDateTime.now());

        return systemAlertRepository.save(alert);
    }

    /**
     * Resolve alert
     */
    public SystemAlert resolveAlert(Long alertId, UUID resolvedBy, String resolutionNotes) {
        log.info("Resolving alert: {} by user: {}", alertId, resolvedBy);

        SystemAlert alert = systemAlertRepository.findById(alertId)
                .orElseThrow(() -> new RuntimeException("Alert not found"));

        alert.setStatus(AlertStatus.RESOLVED);
        alert.setResolvedBy(resolvedBy);
        alert.setResolvedAt(LocalDateTime.now());
        alert.setResolutionNotes(resolutionNotes);

        return systemAlertRepository.save(alert);
    }

    /**
     * Scheduled health check - runs every 5 minutes
     */
    @Scheduled(fixedRate = 300000) // 5 minutes
    public void scheduledHealthCheck() {
        log.info("Running scheduled health check");

        SystemHealthResponseDTO health = getSystemHealth();

        // Generate alerts for unhealthy services
        if (!"HEALTHY".equals(health.getOverallStatus())) {
            health.getServices().forEach((serviceName, serviceHealth) -> {
                if (!"HEALTHY".equals(serviceHealth.getStatus())) {
                    AlertSeverity severity = "CRITICAL".equals(health.getOverallStatus())
                            ? AlertSeverity.CRITICAL
                            : AlertSeverity.HIGH;

                    generateAlert(
                            AlertType.SERVICE_DOWN,
                            severity,
                            serviceName,
                            "Service Health Alert",
                            String.format("%s is %s: %s", serviceName,
                                    serviceHealth.getStatus(), serviceHealth.getMessage())
                    );
                }
            });
        }
    }

    /**
     * Get system metrics for last N hours (simplified - returns empty for now)
     */
    public List<SystemHealthMetric> getMetrics(String serviceName, int hours) {
        log.info("Fetching metrics for {} for last {} hours", serviceName, hours);
        // Returns empty list - metrics collection can be enhanced later
        return new ArrayList<>();
    }

    /**
     * Get all metrics for last N hours (simplified - returns empty for now)
     */
    public List<SystemHealthMetric> getAllMetrics(int hours) {
        log.info("Fetching all metrics for last {} hours", hours);
        // Returns empty list - metrics collection can be enhanced later
        return new ArrayList<>();
    }

    public Map<String, SystemHealthResponseDTO.ServiceHealth> getAllServicesHealth() {
        Map<String, SystemHealthResponseDTO.ServiceHealth> services = new HashMap<>();
        services.put("user-service", checkServiceHealth("user-service", this::checkUserService));
        services.put("stall-service", checkServiceHealth("stall-service", this::checkStallService));
        services.put("reservation-service", checkServiceHealth("reservation-service", this::checkReservationService));
        services.put("notification-service", checkServiceHealth("notification-service", this::checkNotificationService));
        services.put("admin-service-database", checkDatabaseHealth());
        return services;
    }

    public SystemHealthResponseDTO.ServiceHealth getDatabaseHealthOnly() {
        return checkDatabaseHealth();
    }

    @FunctionalInterface
    private interface ServiceChecker {
        boolean check();
    }
}
