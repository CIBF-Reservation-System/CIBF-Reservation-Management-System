package com.cibf.adminservice.admin.DTO.Response;

import com.cibf.adminservice.admin.Common.ServiceStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Response DTO for system health
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SystemHealthResponseDTO {

    private ServiceStatus overallStatus;
    private Map<String, ServiceHealthDTO> services;
    private DatabaseHealthDTO database;
    private Long timestamp;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ServiceHealthDTO {
        private ServiceStatus status;
        private String url;
        private Long responseTime;
        private String errorMessage;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DatabaseHealthDTO {
        private ServiceStatus status;
        private Long activeConnections;
        private String errorMessage;
    }
}

