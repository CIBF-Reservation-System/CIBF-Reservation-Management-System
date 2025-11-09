package com.cibf.adminservice.admin.Client;

import com.cibf.adminservice.admin.DTO.Internal.ReservationServiceDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Fallback implementation for ReservationServiceClient
 * Provides default responses when reservation-service is unavailable
 */
@Slf4j
@Component
public class ReservationServiceClientFallback implements ReservationServiceClient {

    @Override
    public ResponseEntity<List<ReservationServiceDTO>> getAllReservations() {
        log.error("Reservation Service is unavailable - getAllReservations fallback triggered");
        return ResponseEntity.status(503).body(new ArrayList<>());
    }

    @Override
    public ResponseEntity<ReservationServiceDTO> getReservationById(UUID reservationId) {
        log.error("Reservation Service is unavailable - getReservationById fallback triggered for reservationId: {}", reservationId);
        return ResponseEntity.status(503).body(null);
    }

    @Override
    public ResponseEntity<List<ReservationServiceDTO>> getReservationsByUserId(UUID userId) {
        log.error("Reservation Service is unavailable - getReservationsByUserId fallback triggered for userId: {}", userId);
        return ResponseEntity.status(503).body(new ArrayList<>());
    }

    @Override
    public ResponseEntity<List<ReservationServiceDTO>> getReservationsByStallId(UUID stallId) {
        log.error("Reservation Service is unavailable - getReservationsByStallId fallback triggered for stallId: {}", stallId);
        return ResponseEntity.status(503).body(new ArrayList<>());
    }

    @Override
    public ResponseEntity<List<ReservationServiceDTO>> getReservationsByStatus(String status) {
        log.error("Reservation Service is unavailable - getReservationsByStatus fallback triggered for status: {}", status);
        return ResponseEntity.status(503).body(new ArrayList<>());
    }

    @Override
    public ResponseEntity<ReservationServiceDTO> cancelReservation(UUID reservationId) {
        log.error("Reservation Service is unavailable - cancelReservation fallback triggered for reservationId: {}", reservationId);
        return ResponseEntity.status(503).body(null);
    }

    @Override
    public ResponseEntity<ReservationServiceDTO> updateReservationStatus(UUID reservationId, String status) {
        log.error("Reservation Service is unavailable - updateReservationStatus fallback triggered for reservationId: {}", reservationId);
        return ResponseEntity.status(503).body(null);
    }

    @Override
    public ResponseEntity<List<ReservationServiceDTO>> getConflictingReservations() {
        log.error("Reservation Service is unavailable - getConflictingReservations fallback triggered");
        return ResponseEntity.status(503).body(new ArrayList<>());
    }

    @Override
    public ResponseEntity<Object> getReservationStatistics() {
        log.error("Reservation Service is unavailable - getReservationStatistics fallback triggered");
        return ResponseEntity.status(503).body(null);
    }

    @Override
    public ResponseEntity<List<ReservationServiceDTO>> getReservationsByPaymentStatus(String paymentStatus) {
        log.error("Reservation Service is unavailable - getReservationsByPaymentStatus fallback triggered for paymentStatus: {}", paymentStatus);
        return ResponseEntity.status(503).body(new ArrayList<>());
    }

    @Override
    public ResponseEntity<Void> deleteReservation(UUID reservationId) {
        log.error("Reservation Service is unavailable - deleteReservation fallback triggered for reservationId: {}", reservationId);
        return ResponseEntity.status(503).build();
    }

    @Override
    public ResponseEntity<Object> healthCheck() {
        log.error("Reservation Service is unavailable - healthCheck fallback triggered");
        return ResponseEntity.status(503).body(null);
    }
}

