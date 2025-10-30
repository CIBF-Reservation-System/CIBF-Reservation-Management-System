package om.cibf.reservationservice.reservation.common;


import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Getter;
import om.cibf.reservationservice.reservation.dto.ReservationDTO;

@Getter
public class SuccessReservationResponse implements ReservationResponse {
    @JsonUnwrapped
    private final ReservationDTO reservation;
    public SuccessReservationResponse(ReservationDTO reservation) {
        this.reservation = reservation;
    }
}
