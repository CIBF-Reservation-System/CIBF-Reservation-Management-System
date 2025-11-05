package com.cibf.adminservice.admin.Entity;

import com.cibf.adminservice.admin.Common.ServiceStatus;
import com.cibf.adminservice.admin.Common.SystemHealthStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Entity class for system_health_metrics table
 * Store periodic system health check results
 */
@Entity
@Table(name = "system_health_metrics", indexes = {
        @Index(name = "idx_check_timestamp", columnList = "check_timestamp"),
        @Index(name = "idx_overall_status", columnList = "overall_status"),
        @Index(name = "idx_user_service_status", columnList = "user_service_status"),
        @Index(name = "idx_database_status", columnList = "database_status")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SystemHealthMetric {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "metric_id")
    private Long metricId;

    @CreationTimestamp
    @Column(name = "check_timestamp", nullable = false, updatable = false)
    private LocalDateTime checkTimestamp;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_service_status", nullable = false)
    private ServiceStatus userServiceStatus = ServiceStatus.UNKNOWN;

    @Column(name = "user_service_response_time")
    private Integer userServiceResponseTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "stall_service_status", nullable = false)
    private ServiceStatus stallServiceStatus = ServiceStatus.UNKNOWN;

    @Column(name = "stall_service_response_time")
    private Integer stallServiceResponseTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "reservation_service_status", nullable = false)
    private ServiceStatus reservationServiceStatus = ServiceStatus.UNKNOWN;

    @Column(name = "reservation_service_response_time")
    private Integer reservationServiceResponseTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "notification_service_status", nullable = false)
    private ServiceStatus notificationServiceStatus = ServiceStatus.UNKNOWN;

    @Column(name = "notification_service_response_time")
    private Integer notificationServiceResponseTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "database_status", nullable = false)
    private ServiceStatus databaseStatus = ServiceStatus.UNKNOWN;

    @Column(name = "database_response_time")
    private Integer databaseResponseTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "overall_status", nullable = false)
    private SystemHealthStatus overallStatus = SystemHealthStatus.HEALTHY;

    @Column(name = "cpu_usage_percent", precision = 5, scale = 2)
    private BigDecimal cpuUsagePercent;

    @Column(name = "memory_usage_percent", precision = 5, scale = 2)
    private BigDecimal memoryUsagePercent;

    @Column(name = "disk_usage_percent", precision = 5, scale = 2)
    private BigDecimal diskUsagePercent;

    @Column(name = "active_connections")
    private Integer activeConnections;
}

