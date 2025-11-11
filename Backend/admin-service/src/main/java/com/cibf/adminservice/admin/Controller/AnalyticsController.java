package com.cibf.adminservice.admin.Controller;

import com.cibf.adminservice.admin.DTO.Response.AnalyticsSummaryResponseDTO;
import com.cibf.adminservice.admin.DTO.Response.ApiResponse;
import com.cibf.adminservice.admin.Service.AnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controller for analytics and reporting
 * Base URL: /cibf/admin-service/analytics
 */
@RestController
@RequestMapping("/analytics")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
@Tag(name = "Analytics & Reporting", description = "Endpoints for retrieving system analytics and generating reports")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    /**
     * Get analytics dashboard summary
     * GET /cibf/admin-service/analytics/dashboard
     */
    @GetMapping("/dashboard")
    @Operation(summary = "Get dashboard analytics", description = "Retrieve comprehensive analytics dashboard summary with key metrics")
    public ResponseEntity<ApiResponse<AnalyticsSummaryResponseDTO>> getDashboardAnalytics() {
        log.info("GET /analytics/dashboard - Fetching analytics dashboard summary");
        AnalyticsSummaryResponseDTO analytics = analyticsService.getAnalyticsSummary();
        return ResponseEntity.ok(ApiResponse.success("Analytics dashboard retrieved successfully", analytics));
    }

    /**
     * Get detailed statistics from all services
     * GET /cibf/admin-service/analytics/statistics
     */
    @GetMapping("/statistics")
    @Operation(summary = "Get detailed statistics", description = "Retrieve detailed statistics from all microservices")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDetailedStatistics() {
        log.info("GET /analytics/statistics - Fetching detailed statistics");
        Map<String, Object> statistics = analyticsService.getDetailedStatistics();
        return ResponseEntity.ok(ApiResponse.success("Detailed statistics retrieved successfully", statistics));
    }

    /**
     * Get analytics summary (alias for dashboard)
     * GET /cibf/admin-service/analytics/summary
     */
    @GetMapping("/summary")
    @Operation(summary = "Get analytics summary", description = "Retrieve analytics summary (alias for dashboard)")
    public ResponseEntity<ApiResponse<AnalyticsSummaryResponseDTO>> getSummary() {
        log.info("GET /analytics/summary - Fetching analytics summary");
        AnalyticsSummaryResponseDTO analytics = analyticsService.getAnalyticsSummary();
        return ResponseEntity.ok(ApiResponse.success("Analytics summary retrieved successfully", analytics));
    }

    /**
     * Get user registration trends
     * GET /cibf/admin-service/analytics/users/trends
     */
    @GetMapping("/users/trends")
    @Operation(summary = "Get user registration trends", description = "Retrieve user registration trends for past N days")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getUserTrends(
            @RequestParam(defaultValue = "30") int days) {
        log.info("GET /analytics/users/trends?days={} - Fetching user trends", days);
        Map<String, Object> trends = analyticsService.getUserRegistrationTrends(days);
        return ResponseEntity.ok(ApiResponse.success("User registration trends retrieved successfully", trends));
    }

    /**
     * Get stall statistics (aggregated)
     * GET /cibf/admin-service/analytics/stalls/statistics
     */
    @GetMapping("/stalls/statistics")
    @Operation(summary = "Get stall statistics", description = "Retrieve aggregated stall statistics")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getStallStatistics() {
        log.info("GET /analytics/stalls/statistics - Fetching stall statistics");
        Map<String, Object> stats = analyticsService.getStallStatistics();
        return ResponseEntity.ok(ApiResponse.success("Stall statistics retrieved successfully", stats));
    }

    /**
     * Get reservation statistics (aggregated)
     * GET /cibf/admin-service/analytics/reservations/statistics
     */
    @GetMapping("/reservations/statistics")
    @Operation(summary = "Get reservation statistics", description = "Retrieve aggregated reservation statistics")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getReservationStatistics() {
        log.info("GET /analytics/reservations/statistics - Fetching reservation statistics");
        Map<String, Object> stats = analyticsService.getReservationStatistics();
        return ResponseEntity.ok(ApiResponse.success("Reservation statistics retrieved successfully", stats));
    }

    /**
     * Get revenue summary
     * GET /cibf/admin-service/analytics/revenue/summary
     */
    @GetMapping("/revenue/summary")
    @Operation(summary = "Get revenue summary", description = "Retrieve summary of revenue analytics")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getRevenueSummary() {
        log.info("GET /analytics/revenue/summary - Fetching revenue summary");
        Map<String, Object> revenue = analyticsService.getRevenueSummary();
        return ResponseEntity.ok(ApiResponse.success("Revenue summary retrieved successfully", revenue));
    }

    /**
     * Generate custom report
     * GET /cibf/admin-service/analytics/reports/custom
     */
    @GetMapping("/reports/custom")
    @Operation(summary = "Generate custom report", description = "Generate a custom analytics report based on parameters")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getCustomReport(
            @RequestParam(defaultValue = "GENERAL") String type,
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate) {
        log.info("GET /analytics/reports/custom?type={}&fromDate={}&toDate={} - Generating custom report", type, fromDate, toDate);
        Map<String, Object> report = analyticsService.generateCustomReport(type, fromDate, toDate);
        return ResponseEntity.ok(ApiResponse.success("Custom report generated successfully", report));
    }
}
