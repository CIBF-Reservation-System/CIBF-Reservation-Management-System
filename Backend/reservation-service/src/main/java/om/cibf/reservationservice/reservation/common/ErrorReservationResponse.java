package om.cibf.reservationservice.reservation.common;

public class ErrorReservationResponse implements ReservationResponse {
    private final   String errormessage;
    public ErrorReservationResponse(String errormessage) {
        this.errormessage = errormessage;
    }
}
