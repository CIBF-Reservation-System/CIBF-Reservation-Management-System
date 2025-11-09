package com.cibf.adminservice.admin.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Response DTO for system health
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SystemHealthResponseDTO {

    private String overallStatus; // HEALTHY, DEGRADED, CRITICAL
    private LocalDateTime timestamp;
    private Map<String, ServiceHealth> services;
    private Long healthyServices;
    private Long totalServices;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ServiceHealth {
        private String status; // HEALTHY, UNHEALTHY
        private Long responseTime;
        private String message;
        private LocalDateTime lastChecked;
    }
}

