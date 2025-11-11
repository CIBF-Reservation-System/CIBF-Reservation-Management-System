package com.cibf.adminservice.admin.Repository;

import com.cibf.adminservice.admin.Common.SystemHealthStatus;
import com.cibf.adminservice.admin.Entity.SystemHealthMetric;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository interface for SystemHealthMetric entity
 */
@Repository
public interface SystemHealthMetricRepository extends JpaRepository<SystemHealthMetric, Long> {

    Page<SystemHealthMetric> findByOverallStatus(SystemHealthStatus overallStatus, Pageable pageable);

    List<SystemHealthMetric> findByCheckTimestampBetween(LocalDateTime start, LocalDateTime end);

    List<SystemHealthMetric> findTop10ByOrderByCheckTimestampDesc();

    void deleteByCheckTimestampBefore(LocalDateTime dateTime);
}

