package com.cibf.adminservice.admin.Repository;

import com.cibf.adminservice.admin.Entity.AdminSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for AdminSession entity
 */
@Repository
public interface AdminSessionRepository extends JpaRepository<AdminSession, UUID> {

    List<AdminSession> findByAdminId(UUID adminId);

    List<AdminSession> findByAdminIdAndIsActive(UUID adminId, Boolean isActive);

    Optional<AdminSession> findByJwtTokenHash(String jwtTokenHash);

    long countByAdminIdAndIsActive(UUID adminId, Boolean isActive);

    void deleteByExpiresAtBefore(LocalDateTime dateTime);

    List<AdminSession> findByExpiresAtBeforeAndIsActive(LocalDateTime dateTime, Boolean isActive);
}

