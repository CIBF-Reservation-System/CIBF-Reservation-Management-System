package com.cibf.reservationservice.reservation.Service;

import com.cibf.reservationservice.reservation.Entity.Reservation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReservationEventProducer {

    private final KafkaTemplate<String, ReservationEvent> kafkaTemplate;

    private static final String TOPIC = "bookfari.reservation.created";

    public void publishReservationCreated(Reservation reservation) {
        ReservationEvent event = ReservationEvent.builder()
                .eventType("RESERVATION_CREATED")
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
                .build();

        try {
            kafkaTemplate.send(TOPIC, reservation.getReservationId().toString(), event);
            log.info("Published reservation created event for reservation: {}", reservation.getReservationId());
        } catch (Exception e) {
            log.error("Failed to publish reservation created event for reservation: {}", reservation.getReservationId(), e);
        }
    }
}