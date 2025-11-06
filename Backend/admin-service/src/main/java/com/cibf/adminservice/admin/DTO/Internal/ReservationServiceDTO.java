package com.cibf.adminservice.admin.DTO.Internal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Internal DTO for reservation data from reservation-service
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationServiceDTO {

    private UUID reservationId;
    private UUID userId;
    private UUID stallId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer numberOfDays;
    private BigDecimal totalAmount;
    private String status;
    private String paymentStatus;
    private String qrCode;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

