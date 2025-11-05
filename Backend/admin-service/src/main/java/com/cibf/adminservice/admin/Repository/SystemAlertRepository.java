package com.cibf.adminservice.admin.Repository;

import com.cibf.adminservice.admin.Common.AlertSeverity;
import com.cibf.adminservice.admin.Common.AlertStatus;
import com.cibf.adminservice.admin.Common.AlertType;
import com.cibf.adminservice.admin.Entity.SystemAlert;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface for SystemAlert entity
 */
@Repository
public interface SystemAlertRepository extends JpaRepository<SystemAlert, Long> {

    Page<SystemAlert> findByStatus(AlertStatus status, Pageable pageable);

    Page<SystemAlert> findBySeverity(AlertSeverity severity, Pageable pageable);

    Page<SystemAlert> findByAlertType(AlertType alertType, Pageable pageable);

    Page<SystemAlert> findByServiceName(String serviceName, Pageable pageable);

    List<SystemAlert> findByStatusIn(List<AlertStatus> statuses);

    List<SystemAlert> findByStatusAndSeverity(AlertStatus status, AlertSeverity severity);

    List<SystemAlert> findByCreatedAtBeforeAndStatus(LocalDateTime date, AlertStatus status);

    long countByStatus(AlertStatus status);

    long countBySeverity(AlertSeverity severity);
}

