package com.cibf.userservice.user_service.Repository;

import com.cibf.userservice.user_service.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository< Role, Long> {
    Optional<Role> findByRoleName(String roleName);
}
