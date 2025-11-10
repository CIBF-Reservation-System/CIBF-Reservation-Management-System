package com.cibf.adminservice.admin.Controller;

import com.cibf.adminservice.admin.Common.AlertSeverity;
import com.cibf.adminservice.admin.Common.AlertStatus;
import com.cibf.adminservice.admin.DTO.Request.SystemAlertRequestDTO;
import com.cibf.adminservice.admin.DTO.Request.UpdateAlertStatusRequestDTO;
import com.cibf.adminservice.admin.DTO.Response.ApiResponse;
import com.cibf.adminservice.admin.DTO.Response.SystemHealthResponseDTO;
import com.cibf.adminservice.admin.Entity.SystemAlert;
import com.cibf.adminservice.admin.Entity.SystemHealthMetric;
import com.cibf.adminservice.admin.Service.SystemMonitoringService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Controller for system monitoring and health checks
 * Base URL: /cibf/admin-service/monitoring
 */
@RestController
@RequestMapping("/monitoring")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
@Tag(name = "System Monitoring", description = "Endpoints for monitoring system health and managing alerts")
public class SystemMonitoringController {

    private final SystemMonitoringService systemMonitoringService;

    /**
     * Get overall system health
     * GET /cibf/admin-service/monitoring/health
     */
    @GetMapping("/health")
    @Operation(summary = "Get system health", description = "Retrieve overall system health status including all microservices")
    public ResponseEntity<ApiResponse<SystemHealthResponseDTO>> getSystemHealth() {
        log.info("GET /monitoring/health - Checking overall system health");
        SystemHealthResponseDTO health = systemMonitoringService.getSystemHealth();
        return ResponseEntity.ok(ApiResponse.success("System health retrieved successfully", health));
    }

    /**
     * Get all system alerts
     * GET /cibf/admin-service/monitoring/alerts
     */
    @GetMapping("/alerts")
    @Operation(summary = "Get all alerts", description = "Retrieve all system alerts")
    public ResponseEntity<ApiResponse<List<SystemAlert>>> getAllAlerts() {
        log.info("GET /monitoring/alerts - Fetching all system alerts");
        List<SystemAlert> alerts = systemMonitoringService.getAllAlerts();
        return ResponseEntity.ok(ApiResponse.success("Alerts retrieved successfully", alerts));
    }

    /**
     * Get alerts by severity
     * GET /cibf/admin-service/monitoring/alerts/severity/{severity}
     */
    @GetMapping("/alerts/severity/{severity}")
    @Operation(summary = "Get alerts by severity", description = "Retrieve system alerts filtered by severity level")
    public ResponseEntity<ApiResponse<List<SystemAlert>>> getAlertsBySeverity(
            @PathVariable @Parameter(description = "Alert severity level") AlertSeverity severity) {
        log.info("GET /monitoring/alerts/severity/{} - Fetching alerts by severity", severity);
        List<SystemAlert> alerts = systemMonitoringService.getAlertsBySeverity(severity);
        return ResponseEntity.ok(ApiResponse.success("Alerts retrieved successfully", alerts));
    }

    /**
     * Get alerts by status
     * GET /cibf/admin-service/monitoring/alerts/status/{status}
     */
    @GetMapping("/alerts/status/{status}")
    @Operation(summary = "Get alerts by status", description = "Retrieve system alerts filtered by status")
    public ResponseEntity<ApiResponse<List<SystemAlert>>> getAlertsByStatus(
            @PathVariable @Parameter(description = "Alert status") AlertStatus status) {
        log.info("GET /monitoring/alerts/status/{} - Fetching alerts by status", status);
        List<SystemAlert> alerts = systemMonitoringService.getAlertsByStatus(status);
        return ResponseEntity.ok(ApiResponse.success("Alerts retrieved successfully", alerts));
    }

    /**
     * Get recent alerts
     * GET /cibf/admin-service/monitoring/alerts/recent
     */
    @GetMapping("/alerts/recent")
    @Operation(summary = "Get recent alerts", description = "Retrieve most recent system alerts")
    public ResponseEntity<ApiResponse<List<SystemAlert>>> getRecentAlerts(
            @RequestParam(defaultValue = "10") @Parameter(description = "Number of recent alerts") int limit) {
        log.info("GET /monitoring/alerts/recent?limit={} - Fetching recent alerts", limit);
        List<SystemAlert> alerts = systemMonitoringService.getRecentAlerts(limit);
        return ResponseEntity.ok(ApiResponse.success("Recent alerts retrieved successfully", alerts));
    }

    /**
     * Create system alert
     * POST /cibf/admin-service/monitoring/alerts
     */
    @PostMapping("/alerts")
    @Operation(summary = "Create system alert", description = "Create a new system alert manually")
    public ResponseEntity<ApiResponse<SystemAlert>> createAlert(
            @Valid @RequestBody SystemAlertRequestDTO request) {
        log.info("POST /monitoring/alerts - Creating new system alert: {}", request.getTitle());

        SystemAlert alert = systemMonitoringService.generateAlert(
                request.getAlertType(),
                request.getSeverity(),
                request.getServiceName(),
                request.getTitle(),
                request.getMessage()
        );

        return ResponseEntity.ok(ApiResponse.success("Alert created successfully", alert));
    }

    /**
     * Acknowledge alert
     * PUT /cibf/admin-service/monitoring/alerts/{alertId}/acknowledge
     */
    @PutMapping("/alerts/{alertId}/acknowledge")
    @Operation(summary = "Acknowledge alert", description = "Acknowledge a system alert")
    public ResponseEntity<ApiResponse<SystemAlert>> acknowledgeAlert(
            @PathVariable @Parameter(description = "Alert ID") Long alertId,
            @RequestParam @Parameter(description = "Admin user ID") UUID acknowledgedBy) {
        log.info("PUT /monitoring/alerts/{}/acknowledge - Acknowledging alert", alertId);

        SystemAlert alert = systemMonitoringService.acknowledgeAlert(alertId, acknowledgedBy);
        return ResponseEntity.ok(ApiResponse.success("Alert acknowledged successfully", alert));
    }

    /**
     * Resolve alert
     * PUT /cibf/admin-service/monitoring/alerts/{alertId}/resolve
     */
    @PutMapping("/alerts/{alertId}/resolve")
    @Operation(summary = "Resolve alert", description = "Mark an alert as resolved with resolution notes")
    public ResponseEntity<ApiResponse<SystemAlert>> resolveAlert(
            @PathVariable @Parameter(description = "Alert ID") Long alertId,
            @Valid @RequestBody UpdateAlertStatusRequestDTO request) {
        log.info("PUT /monitoring/alerts/{}/resolve - Resolving alert", alertId);

        SystemAlert alert = systemMonitoringService.resolveAlert(
                alertId,
                request.getResolvedBy(),
                request.getResolutionNotes()
        );
        return ResponseEntity.ok(ApiResponse.success("Alert resolved successfully", alert));
    }

    /**
     * Get metrics for specific service
     * GET /cibf/admin-service/monitoring/metrics/{serviceName}
     */
    @GetMapping("/metrics/{serviceName}")
    @Operation(summary = "Get service metrics", description = "Retrieve performance metrics for a specific service")
    public ResponseEntity<ApiResponse<List<SystemHealthMetric>>> getServiceMetrics(
            @PathVariable @Parameter(description = "Service name") String serviceName,
            @RequestParam(defaultValue = "24") @Parameter(description = "Time period in hours") int hours) {
        log.info("GET /monitoring/metrics/{}?hours={} - Fetching service metrics", serviceName, hours);

        List<SystemHealthMetric> metrics = systemMonitoringService.getMetrics(serviceName, hours);
        return ResponseEntity.ok(ApiResponse.success("Metrics retrieved successfully", metrics));
    }

    /**
     * Get all metrics
     * GET /cibf/admin-service/monitoring/metrics
     */
    @GetMapping("/metrics")
    @Operation(summary = "Get all metrics", description = "Retrieve performance metrics for all services")
    public ResponseEntity<ApiResponse<List<SystemHealthMetric>>> getAllMetrics(
            @RequestParam(defaultValue = "24") @Parameter(description = "Time period in hours") int hours) {
        log.info("GET /monitoring/metrics?hours={} - Fetching all metrics", hours);

        List<SystemHealthMetric> metrics = systemMonitoringService.getAllMetrics(hours);
        return ResponseEntity.ok(ApiResponse.success("Metrics retrieved successfully", metrics));
    }

    /**
     * Trigger manual health check
     * POST /cibf/admin-service/monitoring/health/check
     */
    @PostMapping("/health/check")
    @Operation(summary = "Trigger health check", description = "Manually trigger a system-wide health check")
    public ResponseEntity<ApiResponse<SystemHealthResponseDTO>> triggerHealthCheck() {
        log.info("POST /monitoring/health/check - Triggering manual health check");
        SystemHealthResponseDTO health = systemMonitoringService.getSystemHealth();
        return ResponseEntity.ok(ApiResponse.success("Health check completed", health));
    }
}

