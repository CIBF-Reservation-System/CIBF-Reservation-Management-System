package com.cibf.adminservice.admin.Repository;

import com.cibf.adminservice.admin.Entity.UserManagementCache;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for UserManagementCache entity
 */
@Repository
public interface UserManagementCacheRepository extends JpaRepository<UserManagementCache, Long> {

    Optional<UserManagementCache> findByUserId(UUID userId);

    Page<UserManagementCache> findByIsActive(Boolean isActive, Pageable pageable);

    Page<UserManagementCache> findByEmailContaining(String email, Pageable pageable);

    Page<UserManagementCache> findByBusinessNameContaining(String businessName, Pageable pageable);

    boolean existsByUserId(UUID userId);
}

