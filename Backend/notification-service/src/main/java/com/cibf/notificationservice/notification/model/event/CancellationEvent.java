package lk.bookfair.notification.model.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CancellationEvent {

    private String reservationId;
    private String userId;
    private String userEmail;
    private String userName;
    private String businessName;
    private List<StallInfo> stalls;
    private LocalDateTime originalReservationDate;
    private LocalDateTime cancellationDate;
    private Double totalAmount;
    private String cancellationReason;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class StallInfo {
        private String stallName;
        private String stallSize;
        private String location;
        private Double price;
    }
}