package com.cibf.adminservice.admin.Repository;

import com.cibf.adminservice.admin.Entity.HealthCheck;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HealthCheckRepository extends JpaRepository<HealthCheck, Long> {
}

