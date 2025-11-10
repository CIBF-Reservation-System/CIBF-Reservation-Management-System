package com.cibf.adminservice.admin.Controller;

import com.cibf.adminservice.admin.Common.ActionStatus;
import com.cibf.adminservice.admin.Common.AlertSeverity;
import com.cibf.adminservice.admin.Common.LogAction;
import com.cibf.adminservice.admin.DTO.Response.ApiResponse;
import com.cibf.adminservice.admin.DTO.Response.AuditLogResponseDTO;
import com.cibf.adminservice.admin.Service.AuditLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Controller for audit log management operations
 * Base URL: /cibf/admin-service/audit-logs
 */
@RestController
@RequestMapping("/audit-logs")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Audit Log Management", description = "Endpoints for viewing and managing audit logs")
public class AuditLogController {

    private final AuditLogService auditLogService;

    /**
     * Get all audit logs (paginated)
     * GET /cibf/admin-service/audit-logs
     */
    @GetMapping
    @Operation(summary = "Get all audit logs", description = "Retrieve all audit logs with pagination and sorting")
    public ResponseEntity<ApiResponse<Page<AuditLogResponseDTO>>> getAllAuditLogs(
            @RequestParam(defaultValue = "0") @Parameter(description = "Page number (0-based)") int page,
            @RequestParam(defaultValue = "20") @Parameter(description = "Page size") int size,
            @RequestParam(defaultValue = "createdAt") @Parameter(description = "Sort by field") String sortBy,
            @RequestParam(defaultValue = "DESC") @Parameter(description = "Sort direction (ASC/DESC)") String sortDir
    ) {
        log.info("GET /audit-logs - Fetching all audit logs - Page: {}, Size: {}", page, size);

        Sort.Direction direction = sortDir.equalsIgnoreCase("ASC") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        Page<AuditLogResponseDTO> response = auditLogService.getAllAuditLogs(pageable);
        return ResponseEntity.ok(ApiResponse.success("Audit logs retrieved successfully", response));
    }

    /**
     * Get audit log by ID
     * GET /cibf/admin-service/audit-logs/{id}
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get audit log by ID", description = "Retrieve specific audit log by ID")
    public ResponseEntity<ApiResponse<AuditLogResponseDTO>> getAuditLogById(
            @PathVariable @Parameter(description = "Audit log ID") Long id
    ) {
        log.info("GET /audit-logs/{} - Fetching audit log by ID", id);
        AuditLogResponseDTO response = auditLogService.getAuditLogById(id);
        return ResponseEntity.ok(ApiResponse.success("Audit log retrieved successfully", response));
    }

    /**
     * Get audit logs by admin user ID
     * GET /cibf/admin-service/audit-logs/admin/{adminId}
     */
    @GetMapping("/admin/{adminId}")
    @Operation(summary = "Get audit logs by admin", description = "Retrieve audit logs for specific admin user")
    public ResponseEntity<ApiResponse<Page<AuditLogResponseDTO>>> getAuditLogsByAdminId(
            @PathVariable @Parameter(description = "Admin user ID") UUID adminId,
            @RequestParam(defaultValue = "0") @Parameter(description = "Page number") int page,
            @RequestParam(defaultValue = "20") @Parameter(description = "Page size") int size
    ) {
        log.info("GET /audit-logs/admin/{} - Fetching audit logs by admin ID", adminId);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<AuditLogResponseDTO> response = auditLogService.getAuditLogsByAdminId(adminId, pageable);
        return ResponseEntity.ok(ApiResponse.success("Audit logs retrieved successfully", response));
    }

    /**
     * Get audit logs by action type
     * GET /cibf/admin-service/audit-logs/action/{action}
     */
    @GetMapping("/action/{action}")
    @Operation(summary = "Get audit logs by action type", description = "Retrieve audit logs by action type")
    public ResponseEntity<ApiResponse<Page<AuditLogResponseDTO>>> getAuditLogsByAction(
            @PathVariable @Parameter(description = "Action type") LogAction action,
            @RequestParam(defaultValue = "0") @Parameter(description = "Page number") int page,
            @RequestParam(defaultValue = "20") @Parameter(description = "Page size") int size
    ) {
        log.info("GET /audit-logs/action/{} - Fetching audit logs by action type", action);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<AuditLogResponseDTO> response = auditLogService.getAuditLogsByActionType(action, pageable);
        return ResponseEntity.ok(ApiResponse.success("Audit logs retrieved successfully", response));
    }

    /**
     * Get audit logs by entity type
     * GET /cibf/admin-service/audit-logs/entity-type/{entityType}
     */
    @GetMapping("/entity-type/{entityType}")
    @Operation(summary = "Get audit logs by entity type", description = "Retrieve audit logs by entity type")
    public ResponseEntity<ApiResponse<Page<AuditLogResponseDTO>>> getAuditLogsByEntityType(
            @PathVariable @Parameter(description = "Entity type (e.g., User, Stall, Reservation)") String entityType,
            @RequestParam(defaultValue = "0") @Parameter(description = "Page number") int page,
            @RequestParam(defaultValue = "20") @Parameter(description = "Page size") int size
    ) {
        log.info("GET /audit-logs/entity-type/{} - Fetching audit logs by entity type", entityType);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<AuditLogResponseDTO> response = auditLogService.getAuditLogsByEntityType(entityType, pageable);
        return ResponseEntity.ok(ApiResponse.success("Audit logs retrieved successfully", response));
    }

    /**
     * Get audit logs by entity ID
     * GET /cibf/admin-service/audit-logs/entity/{entityId}
     */
    @GetMapping("/entity/{entityId}")
    @Operation(summary = "Get audit logs by entity ID", description = "Retrieve audit logs for specific entity")
    public ResponseEntity<ApiResponse<Page<AuditLogResponseDTO>>> getAuditLogsByEntityId(
            @PathVariable @Parameter(description = "Entity ID") String entityId,
            @RequestParam(defaultValue = "0") @Parameter(description = "Page number") int page,
            @RequestParam(defaultValue = "20") @Parameter(description = "Page size") int size
    ) {
        log.info("GET /audit-logs/entity/{} - Fetching audit logs by entity ID", entityId);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<AuditLogResponseDTO> response = auditLogService.getAuditLogsByEntityId(entityId, pageable);
        return ResponseEntity.ok(ApiResponse.success("Audit logs retrieved successfully", response));
    }

    /**
     * Get audit logs by severity
     * GET /cibf/admin-service/audit-logs/severity/{severity}
     */
    @GetMapping("/severity/{severity}")
    @Operation(summary = "Get audit logs by severity", description = "Retrieve audit logs by severity level")
    public ResponseEntity<ApiResponse<Page<AuditLogResponseDTO>>> getAuditLogsBySeverity(
            @PathVariable @Parameter(description = "Severity level") AlertSeverity severity,
            @RequestParam(defaultValue = "0") @Parameter(description = "Page number") int page,
            @RequestParam(defaultValue = "20") @Parameter(description = "Page size") int size
    ) {
        log.info("GET /audit-logs/severity/{} - Fetching audit logs by severity", severity);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<AuditLogResponseDTO> response = auditLogService.getAuditLogsBySeverity(severity, pageable);
        return ResponseEntity.ok(ApiResponse.success("Audit logs retrieved successfully", response));
    }

    /**
     * Get audit logs by status
     * GET /cibf/admin-service/audit-logs/status/{status}
     */
    @GetMapping("/status/{status}")
    @Operation(summary = "Get audit logs by status", description = "Retrieve audit logs by action status")
    public ResponseEntity<ApiResponse<Page<AuditLogResponseDTO>>> getAuditLogsByStatus(
            @PathVariable @Parameter(description = "Action status") ActionStatus status,
            @RequestParam(defaultValue = "0") @Parameter(description = "Page number") int page,
            @RequestParam(defaultValue = "20") @Parameter(description = "Page size") int size
    ) {
        log.info("GET /audit-logs/status/{} - Fetching audit logs by status", status);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<AuditLogResponseDTO> response = auditLogService.getAuditLogsByStatus(status, pageable);
        return ResponseEntity.ok(ApiResponse.success("Audit logs retrieved successfully", response));
    }

    /**
     * Get audit logs by date range
     * GET /cibf/admin-service/audit-logs/date-range
     */
    @GetMapping("/date-range")
    @Operation(summary = "Get audit logs by date range", description = "Retrieve audit logs within a specific date range")
    public ResponseEntity<ApiResponse<Page<AuditLogResponseDTO>>> getAuditLogsByDateRange(
            @RequestParam @Parameter(description = "Start date (ISO format)")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @Parameter(description = "End date (ISO format)")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(defaultValue = "0") @Parameter(description = "Page number") int page,
            @RequestParam(defaultValue = "20") @Parameter(description = "Page size") int size
    ) {
        log.info("GET /audit-logs/date-range - Fetching audit logs between {} and {}", startDate, endDate);
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<AuditLogResponseDTO> response = auditLogService.getAuditLogsByDateRange(startDate, endDate, pageable);
        return ResponseEntity.ok(ApiResponse.success("Audit logs retrieved successfully", response));
    }

    /**
     * Get recent audit logs (top 10)
     * GET /cibf/admin-service/audit-logs/recent
     */
    @GetMapping("/recent")
    @Operation(summary = "Get recent audit logs", description = "Retrieve the most recent 10 audit logs")
    public ResponseEntity<ApiResponse<List<AuditLogResponseDTO>>> getRecentAuditLogs() {
        log.info("GET /audit-logs/recent - Fetching recent audit logs");
        List<AuditLogResponseDTO> response = auditLogService.getRecentAuditLogs();
        return ResponseEntity.ok(ApiResponse.success("Recent audit logs retrieved successfully", response));
    }

    /**
     * Get audit statistics
     * GET /cibf/admin-service/audit-logs/statistics
     */
    @GetMapping("/statistics")
    @Operation(summary = "Get audit statistics", description = "Retrieve audit log statistics")
    public ResponseEntity<ApiResponse<AuditLogService.AuditStatistics>> getAuditStatistics() {
        log.info("GET /audit-logs/statistics - Fetching audit statistics");
        AuditLogService.AuditStatistics response = auditLogService.getAuditStatistics();
        return ResponseEntity.ok(ApiResponse.success("Audit statistics retrieved successfully", response));
    }

    /**
     * Delete old audit logs (cleanup)
     * DELETE /cibf/admin-service/audit-logs/cleanup
     */
    @DeleteMapping("/cleanup")
    @Operation(summary = "Delete old audit logs", description = "Delete audit logs older than specified date (SUPER_ADMIN only)")
    public ResponseEntity<ApiResponse<Void>> deleteOldAuditLogs(
            @RequestParam @Parameter(description = "Delete logs before this date (ISO format)")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime beforeDate,
            @RequestHeader("X-Admin-Id") UUID adminId
    ) {
        log.info("DELETE /audit-logs/cleanup - Deleting audit logs before: {}", beforeDate);
        auditLogService.deleteOldAuditLogs(beforeDate);
        return ResponseEntity.ok(ApiResponse.success("Old audit logs deleted successfully", null));
    }
}

