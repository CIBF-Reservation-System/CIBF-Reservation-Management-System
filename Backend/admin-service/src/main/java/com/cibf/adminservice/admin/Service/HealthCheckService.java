package com.cibf.adminservice.admin.Service;

import com.cibf.adminservice.admin.DTO.HealthCheckResponseDTO;
import com.cibf.adminservice.admin.Entity.HealthCheck;
import com.cibf.adminservice.admin.Repository.HealthCheckRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class HealthCheckService {

    private final HealthCheckRepository healthCheckRepository;

    @Value("${spring.datasource.url}")
    private String datasourceUrl;

    public HealthCheckService(HealthCheckRepository healthCheckRepository) {
        this.healthCheckRepository = healthCheckRepository;
    }

    public HealthCheckResponseDTO performHealthCheck() {
        try {
            // Test database connection by saving a health check record
            HealthCheck healthCheck = HealthCheck.builder()
                    .serviceName("admin-service")
                    .status("HEALTHY")
                    .message("Database connection successful")
                    .build();

            HealthCheck saved = healthCheckRepository.save(healthCheck);
            long recordCount = healthCheckRepository.count();

            log.info("Health check performed successfully. Record ID: {}, Total records: {}",
                    saved.getId(), recordCount);

            // Extract database name from URL
            String dbName = extractDatabaseName(datasourceUrl);

            return HealthCheckResponseDTO.builder()
                    .serviceName("admin-service")
                    .status("HEALTHY")
                    .message("Service is running and database is connected")
                    .timestamp(LocalDateTime.now())
                    .database(HealthCheckResponseDTO.DatabaseInfo.builder()
                            .connected(true)
                            .url(datasourceUrl)
                            .databaseName(dbName)
                            .recordCount(recordCount)
                            .build())
                    .build();

        } catch (Exception e) {
            log.error("Health check failed: {}", e.getMessage(), e);

            return HealthCheckResponseDTO.builder()
                    .serviceName("admin-service")
                    .status("UNHEALTHY")
                    .message("Database connection failed: " + e.getMessage())
                    .timestamp(LocalDateTime.now())
                    .database(HealthCheckResponseDTO.DatabaseInfo.builder()
                            .connected(false)
                            .url(datasourceUrl)
                            .databaseName("N/A")
                            .recordCount(0)
                            .build())
                    .build();
        }
    }

    private String extractDatabaseName(String url) {
        try {
            // Extract database name from JDBC URL
            // Format: jdbc:mysql://localhost:3306/admin_service_db?params
            String[] parts = url.split("/");
            if (parts.length > 3) {
                String dbPart = parts[3];
                return dbPart.split("\\?")[0];
            }
            return "Unknown";
        } catch (Exception e) {
            return "Unknown";
        }
    }
}

