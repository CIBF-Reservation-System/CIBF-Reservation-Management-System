package om.cibf.reservationservice.reservation.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import om.cibf.reservationservice.reservation.Entity.Reservation;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReservationResponseDTO {

    private UUID reservationId;
    private UUID userId;
    private UUID stallId;
    private String eventName;
    private LocalDateTime reservationDate;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Reservation.ReservationStatus status;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String message;
    private String error;
}