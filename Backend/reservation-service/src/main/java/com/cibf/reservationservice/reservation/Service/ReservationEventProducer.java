package com.cibf.reservationservice.reservation.Service;

import com.cibf.reservationservice.reservation.Entity.Reservation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReservationEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TOPIC = "bookfari.reservation.created";

    public void publishReservationsCreated(List<Reservation> reservations) {
        List<ReservationEvent> events = reservations.stream()
                .map(reservation -> ReservationEvent.builder()
                        .eventType("RESERVATIONS_CREATED")
                        .reservationId(reservation.getReservationId())
                        .userId(reservation.getUserId())
                        .stallId(reservation.getStallId())
                        .businessName(reservation.getBusinessName())
                        .email(reservation.getEmail())
                        .phoneNumber(reservation.getPhoneNumber())
                        .reservationDate(reservation.getReservationDate())
                        .status(reservation.getStatus().toString())
                        .createdAt(reservation.getCreatedAt())
                        .updatedAt(reservation.getUpdatedAt())
                        .build())
                .collect(Collectors.toList());

        try {
            kafkaTemplate.send(TOPIC, "batch", events);
            log.info("Published reservations created event for {} reservations", reservations.size());
        } catch (Exception e) {
            log.error("Failed to publish reservations created event for {} reservations", reservations.size(), e);
        }
    }
}