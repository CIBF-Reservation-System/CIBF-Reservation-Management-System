package com.cibf.adminservice.admin.Repository;

import com.cibf.adminservice.admin.Common.AdminRole;
import com.cibf.adminservice.admin.Entity.AdminUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for AdminUser entity
 */
@Repository
public interface AdminUserRepository extends JpaRepository<AdminUser, UUID> {

    Optional<AdminUser> findByUsername(String username);

    Optional<AdminUser> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    List<AdminUser> findByIsActive(Boolean isActive);

    List<AdminUser> findByRole(AdminRole role);

    List<AdminUser> findByIsActiveAndRole(Boolean isActive, AdminRole role);
}

