package com.cibf.adminservice.admin.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * Dashboard Statistics DTO
 * Contains aggregated statistics for the admin dashboard
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsDTO {
    
    // User Statistics
    private Long totalUsers;
    private Long activeUsers;
    private Long newUsersThisMonth;
    
    // Stall Statistics
    private Long totalStalls;
    private Long availableStalls;
    private Long reservedStalls;
    private Long pendingStalls;
    private Double occupancyRate;
    
    // Reservation Statistics
    private Long totalReservations;
    private Long confirmedReservations;
    private Long pendingReservations;
    private Long cancelledReservations;
    private Double totalRevenue;
    
    // Area-wise breakdown
    private Map<String, Integer> stallsByArea;
    private Map<String, Integer> reservationsByStatus;
    
    // Recent Activity
    private Integer newReservationsToday;
    private Integer newUsersToday;
}
