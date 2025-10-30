package om.cibf.reservationservice.reservation.Controller;


import om.cibf.reservationservice.reservation.Entity.Reservation;
import om.cibf.reservationservice.reservation.common.ReservationResponse;
import om.cibf.reservationservice.reservation.dto.ReservationDTO;
import om.cibf.reservationservice.reservation.dto.ReservationEventDTO;
import om.cibf.reservationservice.reservation.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ReservationController {
    @Autowired
    private ReservationService reservationService;

    @GetMapping( "/allreservations")
    public List<ReservationDTO> getAllReservations() {
        return reservationService.getAllReservations();
    }

    @GetMapping("/reserve/{reserveId}")
    public ReservationDTO getReservation(@PathVariable Integer reserveId) {
        return reservationService.getReservationById(reserveId);
    }

    @PostMapping("/addreservation")
    public ReservationDTO saveStall(@RequestBody ReservationDTO reserverId) {
        return reservationService.saveReservation(reserverId);
    }


    @DeleteMapping("/deleterserve/{reserveId}")
    public String deleteReservation(@PathVariable Integer reserveId) {
        return reservationService.deleteReservation(reserveId);
    }


}
