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
    private String businessName;
    private String email;
    private String phoneNumber;
    private LocalDateTime reservationDate;
    private Reservation.ReservationStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String message;
    private String error;
}