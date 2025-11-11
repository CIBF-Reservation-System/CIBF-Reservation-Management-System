package com.cibf.adminservice.admin.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO for reservation details
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDetailResponseDTO {

    private UUID reservationId;
    private UUID userId;
    private String userName;
    private String userEmail;
    private UUID stallId;
    private String stallName;
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

