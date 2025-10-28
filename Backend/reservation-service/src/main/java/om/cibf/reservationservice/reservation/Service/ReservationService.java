package om.cibf.reservationservice.reservation.Service;

import om.cibf.reservationservice.reservation.DTO.ReservationRequestDTO;
import om.cibf.reservationservice.reservation.DTO.ReservationResponseDTO;
import om.cibf.reservationservice.reservation.Entity.Reservation;
import om.cibf.reservationservice.reservation.Repository.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

            // Check for conflicting reservations
            List<Reservation> conflicts = reservationRepository.findConflictingReservations(
                    request.getStallId(),
                    request.getStartTime(),
                    request.getEndTime()
            );

            if (!conflicts.isEmpty()) {
                return ReservationResponseDTO.builder()
                        .error("Stall is already reserved for the selected time period")
                        .build();
            }

            // Create reservation
            Reservation reservation = Reservation.builder()
                    .userId(request.getUserId())
                    .stallId(request.getStallId())
                    .eventName(request.getEventName())
                    .reservationDate(request.getReservationDate())
                    .startTime(request.getStartTime())
                    .endTime(request.getEndTime())
                    .status(Reservation.ReservationStatus.PENDING)
                    .notes(request.getNotes())
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

        // Check for conflicts if time changed
        if (!request.getStartTime().equals(reservation.getStartTime()) ||
            !request.getEndTime().equals(reservation.getEndTime()) ||
            !request.getStallId().equals(reservation.getStallId())) {

            List<Reservation> conflicts = reservationRepository.findConflictingReservations(
                    request.getStallId(),
                    request.getStartTime(),
                    request.getEndTime()
            );

            // Remove current reservation from conflicts check
            conflicts = conflicts.stream()
                    .filter(conflict -> !conflict.getReservationId().equals(reservationId))
                    .collect(Collectors.toList());

            if (!conflicts.isEmpty()) {
                return ReservationResponseDTO.builder()
                        .error("Stall is already reserved for the selected time period")
                        .build();
            }
        }

        // Update reservation
        reservation.setStallId(request.getStallId());
        reservation.setEventName(request.getEventName());
        reservation.setReservationDate(request.getReservationDate());
        reservation.setStartTime(request.getStartTime());
        reservation.setEndTime(request.getEndTime());
        reservation.setNotes(request.getNotes());

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
                .eventName(reservation.getEventName())
                .reservationDate(reservation.getReservationDate())
                .startTime(reservation.getStartTime())
                .endTime(reservation.getEndTime())
                .status(reservation.getStatus())
                .notes(reservation.getNotes())
                .createdAt(reservation.getCreatedAt())
                .updatedAt(reservation.getUpdatedAt())
                .message(message)
                .build();
    }
}