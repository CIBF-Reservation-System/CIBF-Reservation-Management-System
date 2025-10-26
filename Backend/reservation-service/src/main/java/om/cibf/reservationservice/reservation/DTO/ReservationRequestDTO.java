package om.cibf.reservationservice.reservation.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationRequestDTO {

    private UUID userId;
    private UUID stallId;
    private String eventName;
    private LocalDateTime reservationDate;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String notes;
}