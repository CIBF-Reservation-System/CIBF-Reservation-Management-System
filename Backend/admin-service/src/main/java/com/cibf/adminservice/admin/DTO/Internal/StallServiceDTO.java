package com.cibf.adminservice.admin.DTO.Internal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Internal DTO for stall data from stall-service
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StallServiceDTO {

    private UUID stallId;
    private String stallName;
    private String description;
    private String category;
    private String location;
    private BigDecimal pricePerDay;
    private String status;
    private Boolean isAvailable;
    private UUID ownerId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

