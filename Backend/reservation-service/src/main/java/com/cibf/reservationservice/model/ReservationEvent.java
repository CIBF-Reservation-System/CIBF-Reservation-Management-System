package com.cibf.reservationservice.model;

import java.time.LocalDateTime;

public class ReservationEvent {
    private String eventType; // e.g., "CREATED", "STATUS_UPDATED", "CANCELLED"
    private String reservationId;
    private String userId;
    private String stallId;
    private LocalDateTime timestamp;

    // Constructors, getters, setters
    public ReservationEvent(String eventType, String reservationId, String userId, String stallId) {
        this.eventType = eventType;
        this.reservationId = reservationId;
        this.userId = userId;
        this.stallId = stallId;
        this.timestamp = LocalDateTime.now();
    }

    // Add toString or JSON serialization as needed
    @Override
    public String toString() {
        return "{\"eventType\":\"" + eventType + "\",\"reservationId\":\"" + reservationId + "\",\"userId\":\"" + userId + "\",\"stallId\":\"" + stallId + "\",\"timestamp\":\"" + timestamp + "\"}";
    }
}