package com.cibf.adminservice.admin.Repository;

import com.cibf.adminservice.admin.Common.ActionStatus;
import com.cibf.adminservice.admin.Common.AlertSeverity;
import com.cibf.adminservice.admin.Common.LogAction;
import com.cibf.adminservice.admin.Entity.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Repository interface for AuditLog entity
 */
@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    Page<AuditLog> findByAdminId(UUID adminId, Pageable pageable);

    Page<AuditLog> findByActionType(LogAction actionType, Pageable pageable);

    Page<AuditLog> findByEntityType(String entityType, Pageable pageable);

    Page<AuditLog> findByEntityId(String entityId, Pageable pageable);

    Page<AuditLog> findBySeverity(AlertSeverity severity, Pageable pageable);

    Page<AuditLog> findByStatus(ActionStatus status, Pageable pageable);

    Page<AuditLog> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);

    List<AuditLog> findTop10ByOrderByCreatedAtDesc();

    void deleteByCreatedAtBefore(LocalDateTime date);
}

