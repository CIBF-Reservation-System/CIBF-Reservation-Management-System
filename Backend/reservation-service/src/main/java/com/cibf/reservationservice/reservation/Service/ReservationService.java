package com.cibf.reservationservice.reservation.Service;

import com.cibf.reservationservice.reservation.DTO.ReservationRequestDTO;
import com.cibf.reservationservice.reservation.DTO.ReservationResponseDTO;
import com.cibf.reservationservice.reservation.Entity.Reservation;
import com.cibf.reservationservice.reservation.Repository.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationEventProducer eventProducer;

    public ReservationService(ReservationRepository reservationRepository, ReservationEventProducer eventProducer) {
        this.reservationRepository = reservationRepository;
        this.eventProducer = eventProducer;
    }

    public List<ReservationResponseDTO> createReservations(List<ReservationRequestDTO> requests) {
        List<Reservation> savedReservations = new ArrayList<>();
        List<ReservationResponseDTO> responses = new ArrayList<>();

        for (ReservationRequestDTO request : requests) {
            try {
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

                Reservation saved = reservationRepository.save(reservation);
                savedReservations.add(saved);

                responses.add(mapToResponseDTO(saved, "Reservation created successfully"));

            } catch (Exception e) {
                responses.add(ReservationResponseDTO.builder()
                        .error("Failed to create reservation: " + e.getMessage())
                        .build());
            }
        }

        // Publish Kafka event with the entire array
        if (!savedReservations.isEmpty()) {
            eventProducer.publishReservationsCreated(savedReservations);
        }

        return responses;
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

    public ReservationResponseDTO deleteReservation(UUID reservationId) {
        Optional<Reservation> reservationOpt = reservationRepository.findById(reservationId);
        if (reservationOpt.isEmpty()) {
            return ReservationResponseDTO.builder()
                    .error("Reservation not found")
                    .build();
        }

        Reservation reservation = reservationOpt.get();

        // Only allow deletion of cancelled or pending reservations
        if (reservation.getStatus() == Reservation.ReservationStatus.CONFIRMED ||
            reservation.getStatus() == Reservation.ReservationStatus.COMPLETED) {
            return ReservationResponseDTO.builder()
                    .error("Cannot delete reservation")
                    .build();
        }

        reservationRepository.delete(reservation);
        return ReservationResponseDTO.builder()
                .message("Reservation deleted successfully")
                .error(null)
                .build();
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
