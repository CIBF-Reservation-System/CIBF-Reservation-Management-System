package com.cibf.adminservice.admin.Client;

import com.cibf.adminservice.admin.DTO.Internal.ReservationServiceDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Feign Client for Reservation Service
 * Communicates with reservation-service to fetch and manage reservation data
 */
@FeignClient(
        name = "reservation-service",
        url = "${spring.cloud.openfeign.client.config.reservation-service.url}",
        fallback = ReservationServiceClientFallback.class
)
public interface ReservationServiceClient {

    /**
     * Get all reservations from reservation service
     */
    @GetMapping("/api/v1/reservations")
    ResponseEntity<List<ReservationServiceDTO>> getAllReservations();

    /**
     * Get reservation by ID
     */
    @GetMapping("/api/v1/reservations/{reservationId}")
    ResponseEntity<ReservationServiceDTO> getReservationById(@PathVariable("reservationId") UUID reservationId);

    /**
     * Get reservations by user ID
     */
    @GetMapping("/api/v1/reservations/user/{userId}")
    ResponseEntity<List<ReservationServiceDTO>> getReservationsByUserId(@PathVariable("userId") UUID userId);

    /**
     * Get reservations by stall ID
     */
    @GetMapping("/api/v1/reservations/stall/{stallId}")
    ResponseEntity<List<ReservationServiceDTO>> getReservationsByStallId(@PathVariable("stallId") UUID stallId);

    /**
     * Get reservations by status
     */
    @GetMapping("/api/v1/reservations/status/{status}")
    ResponseEntity<List<ReservationServiceDTO>> getReservationsByStatus(@PathVariable("status") String status);

    /**
     * Cancel reservation
     */
    @PatchMapping("/api/v1/reservations/{reservationId}/cancel")
    ResponseEntity<ReservationServiceDTO> cancelReservation(@PathVariable("reservationId") UUID reservationId);

    /**
     * Update reservation status
     */
    @PatchMapping("/api/v1/reservations/{reservationId}/status")
    ResponseEntity<ReservationServiceDTO> updateReservationStatus(
            @PathVariable("reservationId") UUID reservationId,
            @RequestParam("status") String status
    );

    /**
     * Get conflicting reservations for a stall
     */
    @GetMapping("/api/v1/reservations/conflicts")
    ResponseEntity<List<ReservationServiceDTO>> getConflictingReservations();

    /**
     * Get reservation statistics
     */
    @GetMapping("/api/v1/reservations/statistics")
    ResponseEntity<Object> getReservationStatistics();

    /**
     * Get reservations by payment status
     */
    @GetMapping("/api/v1/reservations/payment-status/{paymentStatus}")
    ResponseEntity<List<ReservationServiceDTO>> getReservationsByPaymentStatus(@PathVariable("paymentStatus") String paymentStatus);

    /**
     * Delete reservation (admin only)
     */
    @DeleteMapping("/api/v1/reservations/{reservationId}")
    ResponseEntity<Void> deleteReservation(@PathVariable("reservationId") UUID reservationId);

    /**
     * Health check endpoint
     */
    @GetMapping("/actuator/health")
    ResponseEntity<Object> healthCheck();
}

