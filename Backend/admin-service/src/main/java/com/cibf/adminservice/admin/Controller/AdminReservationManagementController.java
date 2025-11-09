package com.cibf.adminservice.admin.Controller;

import com.cibf.adminservice.admin.DTO.Internal.ReservationServiceDTO;
import com.cibf.adminservice.admin.DTO.Response.ApiResponse;
import com.cibf.adminservice.admin.Service.ReservationManagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Controller for managing reservations through admin service
 * Base URL: /cibf/admin-service/reservations-management
 */
@RestController
@RequestMapping("/reservations-management")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class AdminReservationManagementController {

    private final ReservationManagementService reservationManagementService;

    /**
     * Get all reservations
     * GET /cibf/admin-service/reservations-management
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<ReservationServiceDTO>>> getAllReservations() {
        log.info("GET /reservations-management - Fetching all reservations");
        List<ReservationServiceDTO> reservations = reservationManagementService.getAllReservations();
        return ResponseEntity.ok(ApiResponse.success("Reservations fetched successfully", reservations));
    }

    /**
     * Get reservation by ID
     * GET /cibf/admin-service/reservations-management/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ReservationServiceDTO>> getReservationById(@PathVariable UUID id) {
        log.info("GET /reservations-management/{} - Fetching reservation details", id);
        ReservationServiceDTO reservation = reservationManagementService.getReservationById(id);
        return ResponseEntity.ok(ApiResponse.success("Reservation details fetched successfully", reservation));
    }

    /**
     * Get reservations by user ID
     * GET /cibf/admin-service/reservations-management/user/{userId}
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<ApiResponse<List<ReservationServiceDTO>>> getReservationsByUserId(@PathVariable UUID userId) {
        log.info("GET /reservations-management/user/{} - Fetching user reservations", userId);
        List<ReservationServiceDTO> reservations = reservationManagementService.getReservationsByUserId(userId);
        return ResponseEntity.ok(ApiResponse.success("User reservations fetched successfully", reservations));
    }

    /**
     * Get reservations by stall ID
     * GET /cibf/admin-service/reservations-management/stall/{stallId}
     */
    @GetMapping("/stall/{stallId}")
    public ResponseEntity<ApiResponse<List<ReservationServiceDTO>>> getReservationsByStallId(@PathVariable UUID stallId) {
        log.info("GET /reservations-management/stall/{} - Fetching stall reservations", stallId);
        List<ReservationServiceDTO> reservations = reservationManagementService.getReservationsByStallId(stallId);
        return ResponseEntity.ok(ApiResponse.success("Stall reservations fetched successfully", reservations));
    }

    /**
     * Get reservations by status
     * GET /cibf/admin-service/reservations-management/status/{status}
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<ReservationServiceDTO>>> getReservationsByStatus(@PathVariable String status) {
        log.info("GET /reservations-management/status/{} - Fetching reservations by status", status);
        List<ReservationServiceDTO> reservations = reservationManagementService.getReservationsByStatus(status);
        return ResponseEntity.ok(ApiResponse.success("Reservations fetched successfully", reservations));
    }

    /**
     * Get reservations by payment status
     * GET /cibf/admin-service/reservations-management/payment-status/{paymentStatus}
     */
    @GetMapping("/payment-status/{paymentStatus}")
    public ResponseEntity<ApiResponse<List<ReservationServiceDTO>>> getReservationsByPaymentStatus(@PathVariable String paymentStatus) {
        log.info("GET /reservations-management/payment-status/{} - Fetching reservations by payment status", paymentStatus);
        List<ReservationServiceDTO> reservations = reservationManagementService.getReservationsByPaymentStatus(paymentStatus);
        return ResponseEntity.ok(ApiResponse.success("Reservations fetched successfully", reservations));
    }

    /**
     * Get conflicting reservations
     * GET /cibf/admin-service/reservations-management/conflicts
     */
    @GetMapping("/conflicts")
    public ResponseEntity<ApiResponse<List<ReservationServiceDTO>>> getConflictingReservations() {
        log.info("GET /reservations-management/conflicts - Fetching conflicting reservations");
        List<ReservationServiceDTO> reservations = reservationManagementService.getConflictingReservations();
        return ResponseEntity.ok(ApiResponse.success("Conflicting reservations fetched successfully", reservations));
    }

    /**
     * Cancel reservation
     * PUT /cibf/admin-service/reservations-management/{id}/cancel
     */
    @PutMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<ReservationServiceDTO>> cancelReservation(@PathVariable UUID id) {
        log.info("PUT /reservations-management/{}/cancel - Cancelling reservation", id);
        ReservationServiceDTO cancelledReservation = reservationManagementService.cancelReservation(id);
        return ResponseEntity.ok(ApiResponse.success("Reservation cancelled successfully", cancelledReservation));
    }

    /**
     * Update reservation status
     * PUT /cibf/admin-service/reservations-management/{id}/status
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<ReservationServiceDTO>> updateReservationStatus(
            @PathVariable UUID id,
            @RequestParam String status) {
        log.info("PUT /reservations-management/{}/status - Updating reservation status to: {}", id, status);
        ReservationServiceDTO updatedReservation = reservationManagementService.updateReservationStatus(id, status);
        return ResponseEntity.ok(ApiResponse.success("Reservation status updated successfully", updatedReservation));
    }

    /**
     * Delete reservation
     * DELETE /cibf/admin-service/reservations-management/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteReservation(@PathVariable UUID id) {
        log.info("DELETE /reservations-management/{} - Deleting reservation", id);
        reservationManagementService.deleteReservation(id);
        return ResponseEntity.ok(ApiResponse.success("Reservation deleted successfully", null));
    }

    /**
     * Get reservation statistics
     * GET /cibf/admin-service/reservations-management/statistics
     */
    @GetMapping("/statistics")
    public ResponseEntity<ApiResponse<Object>> getReservationStatistics() {
        log.info("GET /reservations-management/statistics - Fetching reservation statistics");
        Object statistics = reservationManagementService.getReservationStatistics();
        return ResponseEntity.ok(ApiResponse.success("Reservation statistics fetched successfully", statistics));
    }
}

