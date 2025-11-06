package om.cibf.reservationservice.reservation.model;

import java.time.LocalDateTime;

public class ReservationEvent {
    private String eventType;
    private String reservationId;
    private String userId;
    private String stallId;
    private LocalDateTime timestamp;

    public ReservationEvent(String eventType, String reservationId, String userId, String stallId) {
        this.eventType = eventType;
        this.reservationId = reservationId;
        this.userId = userId;
        this.stallId = stallId;
        this.timestamp = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "{\"eventType\":\"" + eventType + "\",\"reservationId\":\"" + reservationId + "\",\"userId\":\"" + userId + "\",\"stallId\":\"" + stallId + "\",\"timestamp\":\"" + timestamp + "\"}";
    }
}