package com.cibf.adminservice.admin.Controller;

import com.cibf.adminservice.admin.DTO.Response.ApiResponse;
import com.cibf.adminservice.admin.DTO.Response.DashboardStatsDTO;
import com.cibf.adminservice.admin.Service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

/**
 * Unified Admin Controller
 * Provides admin dashboard and management capabilities through delegated service calls
 * Base URL: /api/v1/admin
 */
@RestController
@RequestMapping
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*", allowCredentials = "true")
@Tag(name = "Admin Management", description = "Unified admin dashboard and management endpoints")
public class AdminController {

    private final AdminService adminService;

    // ==================== DASHBOARD ====================

    /**
     * Get dashboard statistics
     * GET /api/v1/admin/dashboard/stats
     */
    @GetMapping("/dashboard/stats")
    @Operation(summary = "Get dashboard statistics", description = "Retrieve overall system statistics for admin dashboard")
    public ResponseEntity<ApiResponse<DashboardStatsDTO>> getDashboardStats() {
        log.info("GET /dashboard/stats - Fetching dashboard statistics");
        DashboardStatsDTO stats = adminService.getDashboardStats();
        return ResponseEntity.ok(ApiResponse.success("Dashboard statistics retrieved successfully", stats));
    }

    // ==================== USER MANAGEMENT ====================

    /**
     * Get all users
     * GET /api/v1/admin/users
     */
    @GetMapping("/users")
    @Operation(summary = "Get all users", description = "Retrieve all users in the system")
    public ResponseEntity<ApiResponse<?>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("GET /users - Fetching all users (page={}, size={})", page, size);
        Object users = adminService.getAllUsers(page, size);
        return ResponseEntity.ok(ApiResponse.success("Users retrieved successfully", users));
    }

    /**
     * Get user by ID
     * GET /api/v1/admin/users/{userId}
     */
    @GetMapping("/users/{userId}")
    @Operation(summary = "Get user by ID", description = "Retrieve specific user details")
    public ResponseEntity<ApiResponse<?>> getUserById(
            @PathVariable @Parameter(description = "User ID") UUID userId) {
        log.info("GET /users/{} - Fetching user details", userId);
        Object user = adminService.getUserById(userId);
        return ResponseEntity.ok(ApiResponse.success("User retrieved successfully", user));
    }

    /**
     * Search users
     * GET /api/v1/admin/users/search
     */
    @GetMapping("/users/search")
    @Operation(summary = "Search users", description = "Search users by name, email or username")
    public ResponseEntity<ApiResponse<?>> searchUsers(
            @RequestParam @Parameter(description = "Search query") String query) {
        log.info("GET /users/search?query={}", query);
        Object users = adminService.searchUsers(query);
        return ResponseEntity.ok(ApiResponse.success("Search completed successfully", users));
    }

    /**
     * Update user status
     * PATCH /api/v1/admin/users/{userId}/status
     */
    @PatchMapping("/users/{userId}/status")
    @Operation(summary = "Update user status", description = "Enable or disable user account")
    public ResponseEntity<ApiResponse<?>> updateUserStatus(
            @PathVariable @Parameter(description = "User ID") UUID userId,
            @RequestParam @Parameter(description = "Active status") Boolean isActive) {
        log.info("PATCH /users/{}/status - Updating user status to: {}", userId, isActive);
        Object user = adminService.updateUserStatus(userId, isActive);
        return ResponseEntity.ok(ApiResponse.success("User status updated successfully", user));
    }

    /**
     * Update user role
     * PATCH /api/v1/admin/users/{userId}/role
     */
    @PatchMapping("/users/{userId}/role")
    @Operation(summary = "Update user role", description = "Update user role in the system")
    public ResponseEntity<ApiResponse<?>> updateUserRole(
            @PathVariable @Parameter(description = "User ID") UUID userId,
            @RequestParam @Parameter(description = "New role") String role) {
        log.info("PATCH /users/{}/role - Updating user role to: {}", userId, role);
        Object user = adminService.updateUserRole(userId, role);
        return ResponseEntity.ok(ApiResponse.success("User role updated successfully", user));
    }

    // ==================== STALL MANAGEMENT ====================

    /**
     * Get all stalls
     * GET /api/v1/admin/stalls
     */
    @GetMapping("/stalls")
    @Operation(summary = "Get all stalls", description = "Retrieve all stalls in the system")
    public ResponseEntity<ApiResponse<?>> getAllStalls(
            @RequestParam(required = false) @Parameter(description = "Filter by area") String area,
            @RequestParam(required = false) @Parameter(description = "Filter by status") String status) {
        log.info("GET /stalls - Fetching all stalls (area={}, status={})", area, status);
        Object stalls = adminService.getAllStalls(area, status);
        return ResponseEntity.ok(ApiResponse.success("Stalls retrieved successfully", stalls));
    }

    /**
     * Get stall by ID
     * GET /api/v1/admin/stalls/{stallId}
     */
    @GetMapping("/stalls/{stallId}")
    @Operation(summary = "Get stall by ID", description = "Retrieve specific stall details")
    public ResponseEntity<ApiResponse<?>> getStallById(
            @PathVariable @Parameter(description = "Stall ID") UUID stallId) {
        log.info("GET /stalls/{} - Fetching stall details", stallId);
        Object stall = adminService.getStallById(stallId);
        return ResponseEntity.ok(ApiResponse.success("Stall retrieved successfully", stall));
    }

    /**
     * Get pending stalls
     * GET /api/v1/admin/stalls/pending
     */
    @GetMapping("/stalls/pending")
    @Operation(summary = "Get pending stalls", description = "Retrieve stalls awaiting approval")
    public ResponseEntity<ApiResponse<?>> getPendingStalls() {
        log.info("GET /stalls/pending - Fetching pending stalls");
        Object stalls = adminService.getPendingStalls();
        return ResponseEntity.ok(ApiResponse.success("Pending stalls retrieved successfully", stalls));
    }

    /**
     * Update stall availability
     * PATCH /api/v1/admin/stalls/{stallId}/availability
     */
    @PatchMapping("/stalls/{stallId}/availability")
    @Operation(summary = "Update stall availability", description = "Update stall availability status")
    public ResponseEntity<ApiResponse<?>> updateStallAvailability(
            @PathVariable @Parameter(description = "Stall ID") UUID stallId,
            @RequestParam @Parameter(description = "Availability status") Boolean isAvailable) {
        log.info("PATCH /stalls/{}/availability - Updating availability to: {}", stallId, isAvailable);
        Object stall = adminService.updateStallAvailability(stallId, isAvailable);
        return ResponseEntity.ok(ApiResponse.success("Stall availability updated successfully", stall));
    }

    /**
     * Approve stall
     * POST /api/v1/admin/stalls/{stallId}/approve
     */
    @PostMapping("/stalls/{stallId}/approve")
    @Operation(summary = "Approve stall", description = "Approve a pending stall")
    public ResponseEntity<ApiResponse<?>> approveStall(
            @PathVariable @Parameter(description = "Stall ID") UUID stallId) {
        log.info("POST /stalls/{}/approve - Approving stall", stallId);
        Object stall = adminService.approveStall(stallId);
        return ResponseEntity.ok(ApiResponse.success("Stall approved successfully", stall));
    }

    /**
     * Reject stall
     * POST /api/v1/admin/stalls/{stallId}/reject
     */
    @PostMapping("/stalls/{stallId}/reject")
    @Operation(summary = "Reject stall", description = "Reject a pending stall")
    public ResponseEntity<ApiResponse<?>> rejectStall(
            @PathVariable @Parameter(description = "Stall ID") UUID stallId,
            @RequestBody(required = false) @Parameter(description = "Rejection reason") Map<String, String> body) {
        String reason = body != null ? body.get("reason") : "No reason provided";
        log.info("POST /stalls/{}/reject - Rejecting stall with reason: {}", stallId, reason);
        Object stall = adminService.rejectStall(stallId, reason);
        return ResponseEntity.ok(ApiResponse.success("Stall rejected successfully", stall));
    }

    // ==================== RESERVATION MANAGEMENT ====================

    /**
     * Get all reservations
     * GET /api/v1/admin/reservations
     */
    @GetMapping("/reservations")
    @Operation(summary = "Get all reservations", description = "Retrieve all reservations in the system")
    public ResponseEntity<ApiResponse<?>> getAllReservations(
            @RequestParam(required = false) @Parameter(description = "Filter by status") String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("GET /reservations - Fetching all reservations (status={}, page={}, size={})", status, page, size);
        Object reservations = adminService.getAllReservations(status, page, size);
        return ResponseEntity.ok(ApiResponse.success("Reservations retrieved successfully", reservations));
    }

    /**
     * Get reservation by ID
     * GET /api/v1/admin/reservations/{reservationId}
     */
    @GetMapping("/reservations/{reservationId}")
    @Operation(summary = "Get reservation by ID", description = "Retrieve specific reservation details")
    public ResponseEntity<ApiResponse<?>> getReservationById(
            @PathVariable @Parameter(description = "Reservation ID") UUID reservationId) {
        log.info("GET /reservations/{} - Fetching reservation details", reservationId);
        Object reservation = adminService.getReservationById(reservationId);
        return ResponseEntity.ok(ApiResponse.success("Reservation retrieved successfully", reservation));
    }

    /**
     * Cancel reservation
     * POST /api/v1/admin/reservations/{reservationId}/cancel
     */
    @PostMapping("/reservations/{reservationId}/cancel")
    @Operation(summary = "Cancel reservation", description = "Cancel a specific reservation")
    public ResponseEntity<ApiResponse<?>> cancelReservation(
            @PathVariable @Parameter(description = "Reservation ID") UUID reservationId,
            @RequestBody(required = false) @Parameter(description = "Cancellation reason") Map<String, String> body) {
        String reason = body != null ? body.get("reason") : "Cancelled by admin";
        log.info("POST /reservations/{}/cancel - Cancelling reservation with reason: {}", reservationId, reason);
        Object reservation = adminService.cancelReservation(reservationId, reason);
        return ResponseEntity.ok(ApiResponse.success("Reservation cancelled successfully", reservation));
    }

    /**
     * Update reservation status
     * PATCH /api/v1/admin/reservations/{reservationId}/status
     */
    @PatchMapping("/reservations/{reservationId}/status")
    @Operation(summary = "Update reservation status", description = "Update reservation status")
    public ResponseEntity<ApiResponse<?>> updateReservationStatus(
            @PathVariable @Parameter(description = "Reservation ID") UUID reservationId,
            @RequestParam @Parameter(description = "New status") String status) {
        log.info("PATCH /reservations/{}/status - Updating status to: {}", reservationId, status);
        Object reservation = adminService.updateReservationStatus(reservationId, status);
        return ResponseEntity.ok(ApiResponse.success("Reservation status updated successfully", reservation));
    }

    // ==================== NOTIFICATION MANAGEMENT ====================

    /**
     * Send notification to user
     * POST /api/v1/admin/notifications/send
     */
    @PostMapping("/notifications/send")
    @Operation(summary = "Send notification", description = "Send notification to specific user or all users")
    public ResponseEntity<ApiResponse<?>> sendNotification(
            @RequestBody @Parameter(description = "Notification details") Map<String, Object> notificationData) {
        log.info("POST /notifications/send - Sending notification");
        Object result = adminService.sendNotification(notificationData);
        return ResponseEntity.ok(ApiResponse.success("Notification sent successfully", result));
    }

    // ==================== SYSTEM HEALTH ====================

    /**
     * Get system health status
     * GET /api/v1/admin/health
     */
    @GetMapping("/health")
    @Operation(summary = "Get system health", description = "Check health status of all services")
    public ResponseEntity<ApiResponse<?>> getSystemHealth() {
        log.info("GET /health - Checking system health");
        Object health = adminService.getSystemHealth();
        return ResponseEntity.ok(ApiResponse.success("System health retrieved successfully", health));
    }
}
