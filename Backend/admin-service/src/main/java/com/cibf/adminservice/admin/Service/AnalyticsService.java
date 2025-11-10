package com.cibf.adminservice.admin.Service;

import com.cibf.adminservice.admin.DTO.Internal.ReservationServiceDTO;
import com.cibf.adminservice.admin.DTO.Internal.StallServiceDTO;
import com.cibf.adminservice.admin.DTO.Internal.UserServiceDTO;
import com.cibf.adminservice.admin.DTO.Response.AnalyticsSummaryResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service for aggregating analytics and dashboard data from all microservices
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AnalyticsService {

    private final UserManagementService userManagementService;
    private final StallManagementService stallManagementService;
    private final ReservationManagementService reservationManagementService;

    /**
     * Get comprehensive analytics summary for admin dashboard
     */
    public AnalyticsSummaryResponseDTO getAnalyticsSummary() {
        log.info("Generating analytics summary");

        try {
            // Fetch data from all services
            List<UserServiceDTO> users = safeGetAllUsers();
            List<StallServiceDTO> stalls = safeGetAllStalls();
            List<ReservationServiceDTO> reservations = safeGetAllReservations();

            // Calculate metrics
            long totalUsers = users.size();
            long activeUsers = users.stream().filter(u -> u.getIsActive() != null && u.getIsActive()).count();

            long totalStalls = stalls.size();
            long activeStalls = stalls.stream().filter(s -> s.getIsAvailable() != null && s.getIsAvailable()).count();
            long pendingStalls = stalls.stream().filter(s -> "PENDING".equalsIgnoreCase(s.getStatus())).count();

            long totalReservations = reservations.size();
            long activeReservations = reservations.stream().filter(r -> "CONFIRMED".equalsIgnoreCase(r.getStatus())).count();
            long pendingReservations = reservations.stream().filter(r -> "PENDING".equalsIgnoreCase(r.getStatus())).count();

            // Calculate total revenue
            BigDecimal totalRevenue = reservations.stream()
                    .filter(r -> r.getTotalAmount() != null)
                    .map(ReservationServiceDTO::getTotalAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // Build response
            AnalyticsSummaryResponseDTO response = AnalyticsSummaryResponseDTO.builder()
                    .totalUsers(totalUsers)
                    .activeUsers(activeUsers)
                    .totalStalls(totalStalls)
                    .activeStalls(activeStalls)
                    .pendingStalls(pendingStalls)
                    .totalReservations(totalReservations)
                    .activeReservations(activeReservations)
                    .pendingReservations(pendingReservations)
                    .totalRevenue(totalRevenue)
                    .build();

            log.info("Analytics summary generated successfully");
            return response;

        } catch (Exception e) {
            log.error("Error generating analytics summary: {}", e.getMessage());
            // Return empty analytics on error
            return AnalyticsSummaryResponseDTO.builder()
                    .totalUsers(0L)
                    .activeUsers(0L)
                    .totalStalls(0L)
                    .activeStalls(0L)
                    .pendingStalls(0L)
                    .totalReservations(0L)
                    .activeReservations(0L)
                    .pendingReservations(0L)
                    .totalRevenue(BigDecimal.ZERO)
                    .build();
        }
    }

    /**
     * Get detailed statistics from all services
     */
    public Map<String, Object> getDetailedStatistics() {
        log.info("Fetching detailed statistics from all services");

        Map<String, Object> statistics = new HashMap<>();

        try {
            statistics.put("users", userManagementService.getUserStatistics());
        } catch (Exception e) {
            log.warn("Failed to fetch user statistics: {}", e.getMessage());
            statistics.put("users", "unavailable");
        }

        try {
            statistics.put("stalls", stallManagementService.getStallStatistics());
        } catch (Exception e) {
            log.warn("Failed to fetch stall statistics: {}", e.getMessage());
            statistics.put("stalls", "unavailable");
        }

        try {
            statistics.put("reservations", reservationManagementService.getReservationStatistics());
        } catch (Exception e) {
            log.warn("Failed to fetch reservation statistics: {}", e.getMessage());
            statistics.put("reservations", "unavailable");
        }

        return statistics;
    }

    public Map<String, Object> getUserRegistrationTrends(int days) {
        log.info("Fetching user registration trends for last {} days", days);
        Map<String, Object> result = new HashMap<>();
        try {
            Object userStats = userManagementService.getUserStatistics();
            result.put("days", days);
            result.put("data", userStats);
        } catch (Exception e) {
            log.warn("User trends unavailable: {}", e.getMessage());
            result.put("data", List.of());
        }
        return result;
    }

    public Map<String, Object> getStallStatistics() {
        log.info("Fetching stall statistics (aggregated)");
        Map<String, Object> result = new HashMap<>();
        try {
            result.put("data", stallManagementService.getStallStatistics());
        } catch (Exception e) {
            log.warn("Stall statistics unavailable: {}", e.getMessage());
            result.put("data", Map.of());
        }
        return result;
    }

    public Map<String, Object> getReservationStatistics() {
        log.info("Fetching reservation statistics (aggregated)");
        Map<String, Object> result = new HashMap<>();
        try {
            result.put("data", reservationManagementService.getReservationStatistics());
        } catch (Exception e) {
            log.warn("Reservation statistics unavailable: {}", e.getMessage());
            result.put("data", Map.of());
        }
        return result;
    }

    public Map<String, Object> getRevenueSummary() {
        log.info("Calculating revenue summary from reservations");
        Map<String, Object> result = new HashMap<>();
        try {
            List<com.cibf.adminservice.admin.DTO.Internal.ReservationServiceDTO> reservations = safeGetAllReservations();
            java.math.BigDecimal total = reservations.stream()
                    .map(r -> r.getTotalAmount() == null ? java.math.BigDecimal.ZERO : r.getTotalAmount())
                    .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
            long count = reservations.size();
            result.put("totalRevenue", total);
            result.put("reservationsCount", count);
        } catch (Exception e) {
            log.warn("Revenue summary unavailable: {}", e.getMessage());
            result.put("totalRevenue", java.math.BigDecimal.ZERO);
            result.put("reservationsCount", 0);
        }
        return result;
    }

    public Map<String, Object> generateCustomReport(String type, String fromDate, String toDate) {
        log.info("Generating custom report: type={}, fromDate={}, toDate={}", type, fromDate, toDate);
        Map<String, Object> report = new HashMap<>();
        report.put("type", type);
        report.put("fromDate", fromDate);
        report.put("toDate", toDate);
        // For now, combine summary + statistics
        report.put("summary", getAnalyticsSummary());
        report.put("statistics", getDetailedStatistics());
        return report;
    }

    // Safe methods that handle exceptions gracefully
    private List<UserServiceDTO> safeGetAllUsers() {
        try {
            return userManagementService.getAllUsers();
        } catch (Exception e) {
            log.warn("Failed to fetch users for analytics: {}", e.getMessage());
            return List.of();
        }
    }

    private List<StallServiceDTO> safeGetAllStalls() {
        try {
            return stallManagementService.getAllStalls();
        } catch (Exception e) {
            log.warn("Failed to fetch stalls for analytics: {}", e.getMessage());
            return List.of();
        }
    }

    private List<ReservationServiceDTO> safeGetAllReservations() {
        try {
            return reservationManagementService.getAllReservations();
        } catch (Exception e) {
            log.warn("Failed to fetch reservations for analytics: {}", e.getMessage());
            return List.of();
        }
    }
}
