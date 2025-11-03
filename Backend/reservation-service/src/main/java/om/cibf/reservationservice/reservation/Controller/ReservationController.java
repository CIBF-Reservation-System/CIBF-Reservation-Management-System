package om.cibf.reservationservice.reservation.Controller;

import om.cibf.reservationservice.reservation.DTO.ReservationRequestDTO;
import om.cibf.reservationservice.reservation.DTO.ReservationResponseDTO;
import om.cibf.reservationservice.reservation.Entity.Reservation;
import om.cibf.reservationservice.reservation.Service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/controller_test")
    public ResponseEntity<String> controllerTest() {
        return ResponseEntity.status(200).body("Reservation Controller is working!");
    }

    // Create a new reservation
    @PostMapping("/create")
    public ResponseEntity<ReservationResponseDTO> createReservation(@RequestBody ReservationRequestDTO request) {
        ReservationResponseDTO response = reservationService.createReservation(request);
        if (response.getError() != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Get all reservations
    @GetMapping
    public ResponseEntity<List<ReservationResponseDTO>> getAllReservations() {
        List<ReservationResponseDTO> reservations = reservationService.getAllReservations();
        return ResponseEntity.ok(reservations);
    }

    // Get reservation by ID
    @GetMapping("/{id}")
    public ResponseEntity<ReservationResponseDTO> getReservation(@PathVariable UUID id) {
        ReservationResponseDTO response = reservationService.getReservationById(id);
        if (response.getError() != null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        return ResponseEntity.ok(response);
    }

    // Get reservations by user ID
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReservationResponseDTO>> getReservationsByUser(@PathVariable UUID userId) {
        List<ReservationResponseDTO> reservations = reservationService.getReservationsByUserId(userId);
        return ResponseEntity.ok(reservations);
    }

    // Update an existing reservation
    @PutMapping("/{id}")
    public ResponseEntity<ReservationResponseDTO> updateReservation(@PathVariable UUID id, @RequestBody ReservationRequestDTO request) {
        ReservationResponseDTO response = reservationService.updateReservation(id, request);
        if (response.getError() != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.ok(response);
    }

    // Cancel a reservation
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<ReservationResponseDTO> cancelReservation(@PathVariable UUID id) {
        ReservationResponseDTO response = reservationService.cancelReservation(id);
        if (response.getError() != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.ok(response);
    }

    // Update reservation status
    @PutMapping("/{id}/status")
    public ResponseEntity<ReservationResponseDTO> updateReservationStatus(@PathVariable UUID id, @RequestBody Reservation.ReservationStatus status) {
        ReservationResponseDTO response = reservationService.updateReservationStatus(id, status);
        if (response.getError() != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.ok(response);
    }

    // Delete a reservation
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReservation(@PathVariable UUID id) {
        boolean deleted = reservationService.deleteReservation(id);
        if (!deleted) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot delete reservation");
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
