package com.cibf.adminservice.admin.Controller;

import com.cibf.adminservice.admin.DTO.Internal.ReservationServiceDTO;
import com.cibf.adminservice.admin.DTO.Internal.StallServiceDTO;
import com.cibf.adminservice.admin.DTO.Internal.UserServiceDTO;
import com.cibf.adminservice.admin.DTO.Response.AnalyticsSummaryResponseDTO;
import com.cibf.adminservice.admin.DTO.Response.ApiResponse;
import com.cibf.adminservice.admin.Service.AnalyticsService;
import com.cibf.adminservice.admin.Service.ReservationManagementService;
import com.cibf.adminservice.admin.Service.StallManagementService;
import com.cibf.adminservice.admin.Service.UserManagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Controller for admin dashboard and inter-service operations
 */
@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
@Slf4j
public class DashboardController {

    private final UserManagementService userManagementService;
    private final StallManagementService stallManagementService;
    private final ReservationManagementService reservationManagementService;
    private final AnalyticsService analyticsService;

    /**
     * Get analytics summary for admin dashboard
     */
    @GetMapping("/analytics")
    public ResponseEntity<ApiResponse<AnalyticsSummaryResponseDTO>> getAnalyticsSummary() {
        log.info("Fetching analytics summary");
        AnalyticsSummaryResponseDTO analytics = analyticsService.getAnalyticsSummary();
        return ResponseEntity.ok(ApiResponse.success("Analytics fetched successfully", analytics));
    }

    /**
     * Get detailed statistics from all services
     */
    @GetMapping("/statistics")
    public ResponseEntity<ApiResponse<Map<String, Object>>> getDetailedStatistics() {
        log.info("Fetching detailed statistics");
        Map<String, Object> statistics = analyticsService.getDetailedStatistics();
        return ResponseEntity.ok(ApiResponse.success("Statistics fetched successfully", statistics));
    }

    /**
     * Get all users
     */
    @GetMapping("/users")
    public ResponseEntity<ApiResponse<List<UserServiceDTO>>> getAllUsers() {
        log.info("Fetching all users");
        List<UserServiceDTO> users = userManagementService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success("Users fetched successfully", users));
    }

    /**
     * Get user by ID
     */
    @GetMapping("/users/{userId}")
    public ResponseEntity<ApiResponse<UserServiceDTO>> getUserById(@PathVariable UUID userId) {
        log.info("Fetching user: {}", userId);
        UserServiceDTO user = userManagementService.getUserById(userId);
        return ResponseEntity.ok(ApiResponse.success("User fetched successfully", user));
    }

    /**
     * Update user status
     */
    @PatchMapping("/users/{userId}/status")
    public ResponseEntity<ApiResponse<UserServiceDTO>> updateUserStatus(
            @PathVariable UUID userId,
            @RequestParam Boolean isActive) {
        log.info("Updating user {} status to: {}", userId, isActive);
        UserServiceDTO user = userManagementService.updateUserStatus(userId, isActive);
        return ResponseEntity.ok(ApiResponse.success("User status updated successfully", user));
    }

    /**
     * Search users
     */
    @GetMapping("/users/search")
    public ResponseEntity<ApiResponse<List<UserServiceDTO>>> searchUsers(@RequestParam String query) {
        log.info("Searching users: {}", query);
        List<UserServiceDTO> users = userManagementService.searchUsers(query);
        return ResponseEntity.ok(ApiResponse.success("Search completed successfully", users));
    }

    /**
     * Get all stalls
     */
    @GetMapping("/stalls")
    public ResponseEntity<ApiResponse<List<StallServiceDTO>>> getAllStalls() {
        log.info("Fetching all stalls");
        List<StallServiceDTO> stalls = stallManagementService.getAllStalls();
        return ResponseEntity.ok(ApiResponse.success("Stalls fetched successfully", stalls));
    }

    /**
     * Get pending approval stalls
     */
    @GetMapping("/stalls/pending")
    public ResponseEntity<ApiResponse<List<StallServiceDTO>>> getPendingStalls() {
        log.info("Fetching pending stalls");
        List<StallServiceDTO> stalls = stallManagementService.getPendingApprovalStalls();
        return ResponseEntity.ok(ApiResponse.success("Pending stalls fetched successfully", stalls));
    }

    /**
     * Approve stall
     */
    @PatchMapping("/stalls/{stallId}/approve")
    public ResponseEntity<ApiResponse<StallServiceDTO>> approveStall(@PathVariable UUID stallId) {
        log.info("Approving stall: {}", stallId);
        StallServiceDTO stall = stallManagementService.approveStall(stallId);
        return ResponseEntity.ok(ApiResponse.success("Stall approved successfully", stall));
    }

    /**
     * Reject stall
     */
    @PatchMapping("/stalls/{stallId}/reject")
    public ResponseEntity<ApiResponse<StallServiceDTO>> rejectStall(
            @PathVariable UUID stallId,
            @RequestParam String reason) {
        log.info("Rejecting stall: {}", stallId);
        StallServiceDTO stall = stallManagementService.rejectStall(stallId, reason);
        return ResponseEntity.ok(ApiResponse.success("Stall rejected successfully", stall));
    }

    /**
     * Get all reservations
     */
    @GetMapping("/reservations")
    public ResponseEntity<ApiResponse<List<ReservationServiceDTO>>> getAllReservations() {
        log.info("Fetching all reservations");
        List<ReservationServiceDTO> reservations = reservationManagementService.getAllReservations();
        return ResponseEntity.ok(ApiResponse.success("Reservations fetched successfully", reservations));
    }

    /**
     * Get reservations by status
     */
    @GetMapping("/reservations/status/{status}")
    public ResponseEntity<ApiResponse<List<ReservationServiceDTO>>> getReservationsByStatus(
            @PathVariable String status) {
        log.info("Fetching reservations with status: {}", status);
        List<ReservationServiceDTO> reservations = reservationManagementService.getReservationsByStatus(status);
        return ResponseEntity.ok(ApiResponse.success("Reservations fetched successfully", reservations));
    }

    /**
     * Cancel reservation
     */
    @PatchMapping("/reservations/{reservationId}/cancel")
    public ResponseEntity<ApiResponse<ReservationServiceDTO>> cancelReservation(
            @PathVariable UUID reservationId) {
        log.info("Cancelling reservation: {}", reservationId);
        ReservationServiceDTO reservation = reservationManagementService.cancelReservation(reservationId);
        return ResponseEntity.ok(ApiResponse.success("Reservation cancelled successfully", reservation));
    }

    /**
     * Get conflicting reservations
     */
    @GetMapping("/reservations/conflicts")
    public ResponseEntity<ApiResponse<List<ReservationServiceDTO>>> getConflictingReservations() {
        log.info("Fetching conflicting reservations");
        List<ReservationServiceDTO> reservations = reservationManagementService.getConflictingReservations();
        return ResponseEntity.ok(ApiResponse.success("Conflicts fetched successfully", reservations));
    }
}

