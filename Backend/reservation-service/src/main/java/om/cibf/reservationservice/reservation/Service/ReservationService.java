package com.cibf.reservationservice.reservation.Service;

import com.cibf.reservationservice.reservation.DTO.ReservationRequestDTO;
import com.cibf.reservationservice.reservation.DTO.ReservationResponseDTO;
import com.cibf.reservationservice.reservation.Entity.Reservation;
import com.cibf.reservationservice.reservation.Repository.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public ReservationResponseDTO createReservation(ReservationRequestDTO request) {
        try {
            // Note: Stall validation should be done via stall service
            // For now, we'll assume the stall exists and is available
            // In a real implementation, you'd call the stall service API

            // Create reservation
            Reservation reservation = Reservation.builder()
                    .userId(request.getUserId())
                    .stallId(request.getStallId())
                    .businessName(request.getBusinessName())
                    .email(request.getEmail())
                    .phoneNumber(request.getPhoneNumber())
                    .reservationDate(LocalDateTime.now())
                    .status(Reservation.ReservationStatus.PENDING)
                    .build();

            Reservation savedReservation = reservationRepository.save(reservation);

            return mapToResponseDTO(savedReservation, "Reservation created successfully");

        } catch (Exception e) {
            return ReservationResponseDTO.builder()
                    .error("Failed to create reservation: " + e.getMessage())
                    .build();
        }
    }

    public List<ReservationResponseDTO> getAllReservations() {
        return reservationRepository.findAll().stream()
                .map(reservation -> mapToResponseDTO(reservation, null))
                .collect(Collectors.toList());
    }

    public ReservationResponseDTO getReservationById(UUID reservationId) {
        Optional<Reservation> reservationOpt = reservationRepository.findById(reservationId);
        if (reservationOpt.isEmpty()) {
            return ReservationResponseDTO.builder()
                    .error("Reservation not found")
                    .build();
        }
        return mapToResponseDTO(reservationOpt.get(), null);
    }

    public List<ReservationResponseDTO> getReservationsByUserId(UUID userId) {
        return reservationRepository.findByUserId(userId).stream()
                .map(reservation -> mapToResponseDTO(reservation, null))
                .collect(Collectors.toList());
    }

    public ReservationResponseDTO updateReservation(UUID reservationId, ReservationRequestDTO request) {
        Optional<Reservation> reservationOpt = reservationRepository.findById(reservationId);
        if (reservationOpt.isEmpty()) {
            return ReservationResponseDTO.builder()
                    .error("Reservation not found")
                    .build();
        }

        Reservation reservation = reservationOpt.get();

        // Check if can update (only pending reservations can be updated)
        if (reservation.getStatus() != Reservation.ReservationStatus.PENDING) {
            return ReservationResponseDTO.builder()
                    .error("Only pending reservations can be updated")
                    .build();
        }

        // Update reservation
        reservation.setStallId(request.getStallId());
        reservation.setBusinessName(request.getBusinessName());
        reservation.setEmail(request.getEmail());
        reservation.setPhoneNumber(request.getPhoneNumber());

        Reservation updatedReservation = reservationRepository.save(reservation);
        return mapToResponseDTO(updatedReservation, "Reservation updated successfully");
    }

    public ReservationResponseDTO cancelReservation(UUID reservationId) {
        Optional<Reservation> reservationOpt = reservationRepository.findById(reservationId);
        if (reservationOpt.isEmpty()) {
            return ReservationResponseDTO.builder()
                    .error("Reservation not found")
                    .build();
        }

        Reservation reservation = reservationOpt.get();

        if (reservation.getStatus() == Reservation.ReservationStatus.CANCELLED) {
            return ReservationResponseDTO.builder()
                    .error("Reservation is already cancelled")
                    .build();
        }

        if (reservation.getStatus() == Reservation.ReservationStatus.COMPLETED) {
            return ReservationResponseDTO.builder()
                    .error("Completed reservations cannot be cancelled")
                    .build();
        }

        reservation.setStatus(Reservation.ReservationStatus.CANCELLED);
        Reservation updatedReservation = reservationRepository.save(reservation);

        return mapToResponseDTO(updatedReservation, "Reservation cancelled successfully");
    }

    public boolean deleteReservation(UUID reservationId) {
        Optional<Reservation> reservationOpt = reservationRepository.findById(reservationId);
        if (reservationOpt.isEmpty()) {
            return false;
        }

        Reservation reservation = reservationOpt.get();

        // Only allow deletion of cancelled or pending reservations
        if (reservation.getStatus() == Reservation.ReservationStatus.CONFIRMED ||
            reservation.getStatus() == Reservation.ReservationStatus.COMPLETED) {
            return false;
        }

        reservationRepository.delete(reservation);
        return true;
    }

    private ReservationResponseDTO mapToResponseDTO(Reservation reservation, String message) {
        return ReservationResponseDTO.builder()
                .reservationId(reservation.getReservationId())
                .userId(reservation.getUserId())
                .stallId(reservation.getStallId())
                .businessName(reservation.getBusinessName())
                .email(reservation.getEmail())
                .phoneNumber(reservation.getPhoneNumber())
                .reservationDate(reservation.getReservationDate())
                .status(reservation.getStatus())
                .createdAt(reservation.getCreatedAt())
                .updatedAt(reservation.getUpdatedAt())
                .message(message)
                .build();
    }
}