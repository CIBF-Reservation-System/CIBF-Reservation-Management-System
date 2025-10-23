package com.cibf.userservice.user_service.Repository;

import com.cibf.userservice.user_service.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository< Role, Long> {
    Role findByRoleName(String roleName);
}
