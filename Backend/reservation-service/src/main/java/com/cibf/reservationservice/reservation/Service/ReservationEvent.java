package com.cibf.reservationservice.reservation.Service;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class ReservationEvent {
    private String eventType;
    private UUID reservationId;
    private UUID userId;
    private UUID stallId;
    private String businessName;
    private String email;
    private String phoneNumber;
    private LocalDateTime reservationDate;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}