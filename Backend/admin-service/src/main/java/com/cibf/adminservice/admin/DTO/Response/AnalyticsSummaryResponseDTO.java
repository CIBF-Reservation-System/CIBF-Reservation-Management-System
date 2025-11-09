package com.cibf.adminservice.admin.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Response DTO for analytics summary
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsSummaryResponseDTO {

    // User metrics
    private Long totalUsers;
    private Long activeUsers;

    // Stall metrics
    private Long totalStalls;
    private Long activeStalls;
    private Long pendingStalls;

    // Reservation metrics
    private Long totalReservations;
    private Long activeReservations;
    private Long pendingReservations;

    // Revenue metrics
    private BigDecimal totalRevenue;
}

