package com.cibf.adminservice.admin.Service;

import com.cibf.adminservice.admin.Client.ReservationServiceClient;
import com.cibf.adminservice.admin.DTO.Internal.ReservationServiceDTO;
import com.cibf.adminservice.admin.Exception.ServiceUnavailableException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Service for managing reservation-related operations via reservation-service
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ReservationManagementService {

    private final ReservationServiceClient reservationServiceClient;

    /**
     * Get all reservations
     */
    public List<ReservationServiceDTO> getAllReservations() {
        log.info("Fetching all reservations from reservation-service");
        try {
            ResponseEntity<List<ReservationServiceDTO>> response = reservationServiceClient.getAllReservations();
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                log.info("Successfully fetched {} reservations", response.getBody().size());
                return response.getBody();
            }
            throw new ServiceUnavailableException("Reservation service returned empty response");
        } catch (Exception e) {
            log.error("Error fetching reservations: {}", e.getMessage());
            throw new ServiceUnavailableException("Reservation service is currently unavailable");
        }
    }

    /**
     * Get reservation by ID
     */
    public ReservationServiceDTO getReservationById(UUID reservationId) {
        log.info("Fetching reservation with ID: {}", reservationId);
        try {
            ResponseEntity<ReservationServiceDTO> response = reservationServiceClient.getReservationById(reservationId);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                log.info("Successfully fetched reservation");
                return response.getBody();
            }
            throw new ServiceUnavailableException("Reservation not found or service unavailable");
        } catch (Exception e) {
            log.error("Error fetching reservation {}: {}", reservationId, e.getMessage());
            throw new ServiceUnavailableException("Reservation service is currently unavailable");
        }
    }

    /**
     * Get reservations by user ID
     */
    public List<ReservationServiceDTO> getReservationsByUserId(UUID userId) {
        log.info("Fetching reservations for user: {}", userId);
        try {
            ResponseEntity<List<ReservationServiceDTO>> response = reservationServiceClient.getReservationsByUserId(userId);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                log.info("Found {} reservations for user", response.getBody().size());
                return response.getBody();
            }
            throw new ServiceUnavailableException("Failed to fetch user reservations");
        } catch (Exception e) {
            log.error("Error fetching user reservations: {}", e.getMessage());
            throw new ServiceUnavailableException("Reservation service is currently unavailable");
        }
    }

    /**
     * Get reservations by stall ID
     */
    public List<ReservationServiceDTO> getReservationsByStallId(UUID stallId) {
        log.info("Fetching reservations for stall: {}", stallId);
        try {
            ResponseEntity<List<ReservationServiceDTO>> response = reservationServiceClient.getReservationsByStallId(stallId);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                log.info("Found {} reservations for stall", response.getBody().size());
                return response.getBody();
            }
            throw new ServiceUnavailableException("Failed to fetch stall reservations");
        } catch (Exception e) {
            log.error("Error fetching stall reservations: {}", e.getMessage());
            throw new ServiceUnavailableException("Reservation service is currently unavailable");
        }
    }

    /**
     * Get reservations by status
     */
    public List<ReservationServiceDTO> getReservationsByStatus(String status) {
        log.info("Fetching reservations with status: {}", status);
        try {
            ResponseEntity<List<ReservationServiceDTO>> response = reservationServiceClient.getReservationsByStatus(status);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                log.info("Found {} reservations with status {}", response.getBody().size(), status);
                return response.getBody();
            }
            throw new ServiceUnavailableException("Failed to fetch reservations by status");
        } catch (Exception e) {
            log.error("Error fetching reservations by status: {}", e.getMessage());
            throw new ServiceUnavailableException("Reservation service is currently unavailable");
        }
    }

    /**
     * Cancel reservation
     */
    public ReservationServiceDTO cancelReservation(UUID reservationId) {
        log.info("Cancelling reservation: {}", reservationId);
        try {
            ResponseEntity<ReservationServiceDTO> response = reservationServiceClient.cancelReservation(reservationId);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                log.info("Successfully cancelled reservation");
                return response.getBody();
            }
            throw new ServiceUnavailableException("Failed to cancel reservation");
        } catch (Exception e) {
            log.error("Error cancelling reservation: {}", e.getMessage());
            throw new ServiceUnavailableException("Reservation service is currently unavailable");
        }
    }

    /**
     * Update reservation status
     */
    public ReservationServiceDTO updateReservationStatus(UUID reservationId, String status) {
        log.info("Updating reservation {} status to: {}", reservationId, status);
        try {
            ResponseEntity<ReservationServiceDTO> response = reservationServiceClient.updateReservationStatus(reservationId, status);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                log.info("Successfully updated reservation status");
                return response.getBody();
            }
            throw new ServiceUnavailableException("Failed to update reservation status");
        } catch (Exception e) {
            log.error("Error updating reservation status: {}", e.getMessage());
            throw new ServiceUnavailableException("Reservation service is currently unavailable");
        }
    }

    /**
     * Get reservations by payment status
     */
    public List<ReservationServiceDTO> getReservationsByPaymentStatus(String paymentStatus) {
        log.info("Fetching reservations with payment status: {}", paymentStatus);
        try {
            ResponseEntity<List<ReservationServiceDTO>> response = reservationServiceClient.getReservationsByPaymentStatus(paymentStatus);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                log.info("Found {} reservations with payment status {}", response.getBody().size(), paymentStatus);
                return response.getBody();
            }
            throw new ServiceUnavailableException("Failed to fetch reservations by payment status");
        } catch (Exception e) {
            log.error("Error fetching reservations by payment status: {}", e.getMessage());
            throw new ServiceUnavailableException("Reservation service is currently unavailable");
        }
    }

    /**
     * Delete reservation
     */
    public void deleteReservation(UUID reservationId) {
        log.info("Deleting reservation: {}", reservationId);
        try {
            ResponseEntity<Void> response = reservationServiceClient.deleteReservation(reservationId);
            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Successfully deleted reservation");
            } else {
                throw new ServiceUnavailableException("Failed to delete reservation");
            }
        } catch (Exception e) {
            log.error("Error deleting reservation: {}", e.getMessage());
            throw new ServiceUnavailableException("Reservation service is currently unavailable");
        }
    }

    /**
     * Get conflicting reservations
     */
    public List<ReservationServiceDTO> getConflictingReservations() {
        log.info("Fetching conflicting reservations");
        try {
            ResponseEntity<List<ReservationServiceDTO>> response = reservationServiceClient.getConflictingReservations();
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                log.info("Found {} conflicting reservations", response.getBody().size());
                return response.getBody();
            }
            throw new ServiceUnavailableException("Failed to fetch conflicting reservations");
        } catch (Exception e) {
            log.error("Error fetching conflicting reservations: {}", e.getMessage());
            throw new ServiceUnavailableException("Reservation service is currently unavailable");
        }
    }

    /**
     * Get reservation statistics
     */
    public Object getReservationStatistics() {
        log.info("Fetching reservation statistics");
        try {
            ResponseEntity<Object> response = reservationServiceClient.getReservationStatistics();
            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Successfully fetched reservation statistics");
                return response.getBody();
            }
            throw new ServiceUnavailableException("Failed to fetch statistics");
        } catch (Exception e) {
            log.error("Error fetching reservation statistics: {}", e.getMessage());
            throw new ServiceUnavailableException("Reservation service is currently unavailable");
        }
    }
}

