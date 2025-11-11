package com.cibf.adminservice.admin.Service;

import com.cibf.adminservice.admin.Common.ActionStatus;
import com.cibf.adminservice.admin.Common.AlertSeverity;
import com.cibf.adminservice.admin.Common.LogAction;
import com.cibf.adminservice.admin.DTO.Response.AuditLogResponseDTO;
import com.cibf.adminservice.admin.Entity.AdminUser;
import com.cibf.adminservice.admin.Entity.AuditLog;
import com.cibf.adminservice.admin.Repository.AdminUserRepository;
import com.cibf.adminservice.admin.Repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service for managing audit logs
 * Provides audit trail functionality for all admin actions
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final AdminUserRepository adminUserRepository;

    /**
     * Log an admin action asynchronously
     */
    @Async
    @Transactional
    public void logAction(
            UUID adminId,
            LogAction actionType,
            String entityType,
            String entityId,
            String description,
            String ipAddress,
            String oldValue,
            AlertSeverity severity,
            ActionStatus status,
            String errorMessage
    ) {
        try {
            AuditLog auditLog = new AuditLog();
            auditLog.setAdminId(adminId);
            auditLog.setActionType(actionType);
            auditLog.setEntityType(entityType);
            auditLog.setEntityId(entityId);
            auditLog.setDescription(description);
            auditLog.setIpAddress(ipAddress);
            auditLog.setOldValue(oldValue);
            auditLog.setSeverity(severity);
            auditLog.setStatus(status);
            auditLog.setErrorMessage(errorMessage);

            auditLogRepository.save(auditLog);
            log.debug("Audit log saved: {} - {}", actionType, description);
        } catch (Exception e) {
            log.error("Failed to save audit log: {}", e.getMessage(), e);
        }
    }

    /**
     * Log an admin action with user agent
     */
    @Async
    @Transactional
    public void logAction(
            UUID adminId,
            LogAction actionType,
            String entityType,
            String entityId,
            String description,
            String ipAddress,
            String userAgent,
            String oldValue,
            String newValue,
            AlertSeverity severity,
            ActionStatus status,
            String errorMessage
    ) {
        try {
            AuditLog auditLog = new AuditLog();
            auditLog.setAdminId(adminId);
            auditLog.setActionType(actionType);
            auditLog.setEntityType(entityType);
            auditLog.setEntityId(entityId);
            auditLog.setDescription(description);
            auditLog.setIpAddress(ipAddress);
            auditLog.setUserAgent(userAgent);
            auditLog.setOldValue(oldValue);
            auditLog.setNewValue(newValue);
            auditLog.setSeverity(severity);
            auditLog.setStatus(status);
            auditLog.setErrorMessage(errorMessage);

            auditLogRepository.save(auditLog);
            log.debug("Audit log saved: {} - {}", actionType, description);
        } catch (Exception e) {
            log.error("Failed to save audit log: {}", e.getMessage(), e);
        }
    }

    /**
     * Get all audit logs with pagination
     */
    public Page<AuditLogResponseDTO> getAllAuditLogs(Pageable pageable) {
        log.info("Fetching all audit logs - Page: {}, Size: {}", pageable.getPageNumber(), pageable.getPageSize());

        Page<AuditLog> auditLogs = auditLogRepository.findAll(pageable);
        log.info("Found {} audit logs", auditLogs.getTotalElements());

        return auditLogs.map(this::mapToResponseDTO);
    }

    /**
     * Get audit log by ID
     */
    public AuditLogResponseDTO getAuditLogById(Long logId) {
        log.info("Fetching audit log by ID: {}", logId);

        AuditLog auditLog = auditLogRepository.findById(logId)
                .orElseThrow(() -> new RuntimeException("Audit log not found"));

        return mapToResponseDTO(auditLog);
    }

    /**
     * Get audit logs by admin ID
     */
    public Page<AuditLogResponseDTO> getAuditLogsByAdminId(UUID adminId, Pageable pageable) {
        log.info("Fetching audit logs for admin: {}", adminId);

        Page<AuditLog> auditLogs = auditLogRepository.findByAdminId(adminId, pageable);
        log.info("Found {} audit logs for admin {}", auditLogs.getTotalElements(), adminId);

        return auditLogs.map(this::mapToResponseDTO);
    }

    /**
     * Get audit logs by action type
     */
    public Page<AuditLogResponseDTO> getAuditLogsByActionType(LogAction actionType, Pageable pageable) {
        log.info("Fetching audit logs by action type: {}", actionType);

        Page<AuditLog> auditLogs = auditLogRepository.findByActionType(actionType, pageable);
        log.info("Found {} audit logs for action type {}", auditLogs.getTotalElements(), actionType);

        return auditLogs.map(this::mapToResponseDTO);
    }

    /**
     * Get audit logs by entity type
     */
    public Page<AuditLogResponseDTO> getAuditLogsByEntityType(String entityType, Pageable pageable) {
        log.info("Fetching audit logs by entity type: {}", entityType);

        Page<AuditLog> auditLogs = auditLogRepository.findByEntityType(entityType, pageable);
        log.info("Found {} audit logs for entity type {}", auditLogs.getTotalElements(), entityType);

        return auditLogs.map(this::mapToResponseDTO);
    }

    /**
     * Get audit logs by entity ID
     */
    public Page<AuditLogResponseDTO> getAuditLogsByEntityId(String entityId, Pageable pageable) {
        log.info("Fetching audit logs by entity ID: {}", entityId);

        Page<AuditLog> auditLogs = auditLogRepository.findByEntityId(entityId, pageable);
        log.info("Found {} audit logs for entity ID {}", auditLogs.getTotalElements(), entityId);

        return auditLogs.map(this::mapToResponseDTO);
    }

    /**
     * Get audit logs by severity
     */
    public Page<AuditLogResponseDTO> getAuditLogsBySeverity(AlertSeverity severity, Pageable pageable) {
        log.info("Fetching audit logs by severity: {}", severity);

        Page<AuditLog> auditLogs = auditLogRepository.findBySeverity(severity, pageable);
        log.info("Found {} audit logs with severity {}", auditLogs.getTotalElements(), severity);

        return auditLogs.map(this::mapToResponseDTO);
    }

    /**
     * Get audit logs by status
     */
    public Page<AuditLogResponseDTO> getAuditLogsByStatus(ActionStatus status, Pageable pageable) {
        log.info("Fetching audit logs by status: {}", status);

        Page<AuditLog> auditLogs = auditLogRepository.findByStatus(status, pageable);
        log.info("Found {} audit logs with status {}", auditLogs.getTotalElements(), status);

        return auditLogs.map(this::mapToResponseDTO);
    }

    /**
     * Get audit logs by date range
     */
    public Page<AuditLogResponseDTO> getAuditLogsByDateRange(
            LocalDateTime startDate,
            LocalDateTime endDate,
            Pageable pageable
    ) {
        log.info("Fetching audit logs between {} and {}", startDate, endDate);

        Page<AuditLog> auditLogs = auditLogRepository.findByCreatedAtBetween(startDate, endDate, pageable);
        log.info("Found {} audit logs in date range", auditLogs.getTotalElements());

        return auditLogs.map(this::mapToResponseDTO);
    }

    /**
     * Get recent audit logs (top 10)
     */
    public List<AuditLogResponseDTO> getRecentAuditLogs() {
        log.info("Fetching recent audit logs");

        List<AuditLog> auditLogs = auditLogRepository.findTop10ByOrderByCreatedAtDesc();
        log.info("Found {} recent audit logs", auditLogs.size());

        return auditLogs.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Delete old audit logs (cleanup)
     */
    @Transactional
    public void deleteOldAuditLogs(LocalDateTime beforeDate) {
        log.info("Deleting audit logs before: {}", beforeDate);

        try {
            auditLogRepository.deleteByCreatedAtBefore(beforeDate);
            log.info("Old audit logs deleted successfully");
        } catch (Exception e) {
            log.error("Failed to delete old audit logs: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to delete old audit logs", e);
        }
    }

    /**
     * Get audit statistics
     */
    public AuditStatistics getAuditStatistics() {
        log.info("Calculating audit statistics");

        long totalLogs = auditLogRepository.count();
        long successfulActions = auditLogRepository.findByStatus(ActionStatus.SUCCESS, Pageable.unpaged()).getTotalElements();
        long failedActions = auditLogRepository.findByStatus(ActionStatus.FAILED, Pageable.unpaged()).getTotalElements();
        long highSeverityLogs = auditLogRepository.findBySeverity(AlertSeverity.HIGH, Pageable.unpaged()).getTotalElements();
        long criticalSeverityLogs = auditLogRepository.findBySeverity(AlertSeverity.CRITICAL, Pageable.unpaged()).getTotalElements();

        AuditStatistics stats = new AuditStatistics();
        stats.setTotalLogs(totalLogs);
        stats.setSuccessfulActions(successfulActions);
        stats.setFailedActions(failedActions);
        stats.setHighSeverityLogs(highSeverityLogs);
        stats.setCriticalSeverityLogs(criticalSeverityLogs);

        log.info("Audit statistics: Total={}, Success={}, Failed={}", totalLogs, successfulActions, failedActions);

        return stats;
    }

    /**
     * Map entity to response DTO
     */
    private AuditLogResponseDTO mapToResponseDTO(AuditLog auditLog) {
        AuditLogResponseDTO dto = new AuditLogResponseDTO();
        dto.setLogId(auditLog.getLogId());
        dto.setAdminId(auditLog.getAdminId());
        dto.setActionType(auditLog.getActionType());
        dto.setEntityType(auditLog.getEntityType());
        dto.setEntityId(auditLog.getEntityId());
        dto.setDescription(auditLog.getDescription());
        dto.setIpAddress(auditLog.getIpAddress());
        dto.setSeverity(auditLog.getSeverity());
        dto.setStatus(auditLog.getStatus());
        dto.setErrorMessage(auditLog.getErrorMessage());
        dto.setCreatedAt(auditLog.getCreatedAt());

        // Fetch admin username if adminId exists
        if (auditLog.getAdminId() != null) {
            Optional<AdminUser> adminUser = adminUserRepository.findById(auditLog.getAdminId());
            adminUser.ifPresent(user -> dto.setAdminUsername(user.getUsername()));
        }

        return dto;
    }

    /**
     * Inner class for audit statistics
     */
    @lombok.Data
    public static class AuditStatistics {
        private long totalLogs;
        private long successfulActions;
        private long failedActions;
        private long highSeverityLogs;
        private long criticalSeverityLogs;
    }
}

