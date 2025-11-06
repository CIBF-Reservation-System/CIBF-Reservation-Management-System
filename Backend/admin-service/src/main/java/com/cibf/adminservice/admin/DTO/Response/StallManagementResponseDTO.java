package com.cibf.adminservice.admin.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO for stall management (from stall-service cache)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StallManagementResponseDTO {

    private UUID stallId;
    private String stallName;
    private String description;
    private String category;
    private String location;
    private BigDecimal pricePerDay;
    private String status;
    private Boolean isAvailable;
    private UUID ownerId;
    private String ownerName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

