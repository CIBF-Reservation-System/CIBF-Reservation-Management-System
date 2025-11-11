package com.cibf.reservationservice.reservation.Service;

import com.cibf.reservationservice.reservation.DTO.ReservationRequestDTO;
import com.cibf.reservationservice.reservation.DTO.ReservationResponseDTO;
import com.cibf.reservationservice.reservation.Entity.Reservation;
import com.cibf.reservationservice.reservation.Repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationEventProducer eventProducer;
    private final RestTemplate restTemplate;

    private static final String STALL_SERVICE_BASE_URL = "http://host.docker.internal:8086/api/v1/stall/updateavailability";

    @Autowired
    public ReservationService(ReservationRepository reservationRepository, ReservationEventProducer eventProducer, RestTemplate restTemplate) {
        this.reservationRepository = reservationRepository;
        this.eventProducer = eventProducer;
        this.restTemplate = restTemplate;
    }

    private boolean checkAndBookStall(String stallId) {
        try {
            String url = STALL_SERVICE_BASE_URL + "/" + stallId;
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, null, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                String responseBody = response.getBody();
                if (responseBody != null && responseBody.contains("Stall availability updated to 0")) {
                    return true; // Successfully booked
                } else if (responseBody != null && responseBody.contains("Stall is not available")) {
                    return false; // Stall not available
                }
            }
        } catch (Exception e) {
            System.err.println("Error checking/booking stall: " + e.getMessage());
        }
        return false;
    }

    private boolean bookStall(String stallId) {
        try {
            String url = STALL_SERVICE_BASE_URL + "/book";
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("stallId", stallId);
            requestBody.put("isBooked", true);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.PUT, entity, Map.class);

            return response.getStatusCode().is2xxSuccessful();
        } catch (Exception e) {
            System.err.println("Error booking stall: " + e.getMessage());
            return false;
        }
    }

    private boolean cancelStallBooking(String stallId) {
        try {
            String url = "http://host.docker.internal:8086/api/v1/stall/cancelreservation/" + stallId;
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, null, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                String responseBody = response.getBody();
                if (responseBody != null && responseBody.contains("stall is now available")) {
                    return true; // Successfully made available
                }
            }
        } catch (Exception e) {
            System.err.println("Error canceling stall booking: " + e.getMessage());
        }
        return false;
    }

    public List<ReservationResponseDTO> createReservations(List<ReservationRequestDTO> requests) {
        List<Reservation> savedReservations = new ArrayList<>();
        List<ReservationResponseDTO> responses = new ArrayList<>();

        // First, validate that the entire batch won't exceed user limits
        Map<UUID, List<ReservationRequestDTO>> requestsByUser = requests.stream()
                .collect(Collectors.groupingBy(ReservationRequestDTO::getUserId));

        for (Map.Entry<UUID, List<ReservationRequestDTO>> entry : requestsByUser.entrySet()) {
            UUID userId = entry.getKey();
            List<ReservationRequestDTO> userRequests = entry.getValue();

            long existingReservations = reservationRepository.findByUserId(userId).size();
            long totalAfterBatch = existingReservations + userRequests.size();

            if (totalAfterBatch > 3) {
                // Reject entire batch - create error responses for all requests
                for (ReservationRequestDTO request : requests) {
                    responses.add(ReservationResponseDTO.builder()
                            .error("Batch contains requests that would exceed the maximum of 3 reservations per user. Current: " + existingReservations + ", requested: " + userRequests.size() + ", total would be: " + totalAfterBatch)
                            .build());
                }
                return responses;
            }
        }

        // If validation passes, proceed with individual processing
        for (ReservationRequestDTO request : requests) {
            try {
                // Check stall availability and book it in one call
                if (!checkAndBookStall(request.getStallId().toString())) {
                    responses.add(ReservationResponseDTO.builder()
                            .error("Stall " + request.getStallId() + " is not available")
                            .build());
                    continue;
                }

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

    public ReservationResponseDTO deleteReservation(UUID reservationId) {
        Optional<Reservation> reservationOpt = reservationRepository.findById(reservationId);
        if (reservationOpt.isEmpty()) {
            return ReservationResponseDTO.builder()
                    .error("Reservation not found")
                    .build();
        }

        Reservation reservation = reservationOpt.get();

        // Try to cancel the stall booking before deleting the reservation
        // For testing purposes, we'll continue even if this fails
        boolean stallCancelled = cancelStallBooking(reservation.getStallId().toString());
        if (!stallCancelled) {
            System.out.println("Warning: Failed to cancel stall booking, but proceeding with reservation deletion");
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
