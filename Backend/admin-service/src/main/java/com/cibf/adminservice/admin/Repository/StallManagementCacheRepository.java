package com.cibf.adminservice.admin.Repository;

import com.cibf.adminservice.admin.Entity.StallManagementCache;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for StallManagementCache entity
 */
@Repository
public interface StallManagementCacheRepository extends JpaRepository<StallManagementCache, Long> {

    Optional<StallManagementCache> findByStallId(UUID stallId);

    Page<StallManagementCache> findByOwnerId(UUID ownerId, Pageable pageable);

    Page<StallManagementCache> findByStatus(String status, Pageable pageable);

    Page<StallManagementCache> findByApprovalStatus(String approvalStatus, Pageable pageable);

    Page<StallManagementCache> findByCategory(String category, Pageable pageable);

    Page<StallManagementCache> findByStallNameContaining(String stallName, Pageable pageable);

    boolean existsByStallId(UUID stallId);

    long countByStatus(String status);

    long countByApprovalStatus(String approvalStatus);
}

