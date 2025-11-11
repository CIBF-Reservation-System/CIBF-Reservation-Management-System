package com.cibf.adminservice.admin.DTO;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HealthCheckResponseDTO {
    private String serviceName;
    private String status;
    private String message;
    private LocalDateTime timestamp;
    private DatabaseInfo database;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DatabaseInfo {
        private boolean connected;
        private String url;
        private String databaseName;
        private long recordCount;
    }
}

