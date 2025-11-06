package com.cibf.adminservice.admin.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Response DTO for analytics summary
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticsSummaryResponseDTO {

    private UserAnalyticsDTO users;
    private StallAnalyticsDTO stalls;
    private ReservationAnalyticsDTO reservations;
    private RevenueAnalyticsDTO revenue;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserAnalyticsDTO {
        private Long totalUsers;
        private Long activeUsers;
        private Long inactiveUsers;
        private Long newUsersToday;
        private Long newUsersThisMonth;
        private Map<String, Long> usersByRole;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StallAnalyticsDTO {
        private Long totalStalls;
        private Long availableStalls;
        private Long bookedStalls;
        private Long pendingApproval;
        private Map<String, Long> stallsByCategory;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReservationAnalyticsDTO {
        private Long totalReservations;
        private Long activeReservations;
        private Long completedReservations;
        private Long cancelledReservations;
        private Long todayReservations;
        private Long thisMonthReservations;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RevenueAnalyticsDTO {
        private BigDecimal totalRevenue;
        private BigDecimal todayRevenue;
        private BigDecimal thisMonthRevenue;
        private BigDecimal thisYearRevenue;
    }
}

