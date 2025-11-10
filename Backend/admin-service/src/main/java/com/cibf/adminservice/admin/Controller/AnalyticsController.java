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
}

