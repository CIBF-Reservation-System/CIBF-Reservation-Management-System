package om.cibf.reservationservice.reservation.Repository;

import om.cibf.reservationservice.reservation.Entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ReservationRepository extends JpaRepository<Reservation, UUID> {

    List<Reservation> findByUserId(UUID userId);

    List<Reservation> findByStallId(UUID stallId);

    List<Reservation> findByStatus(Reservation.ReservationStatus status);

    @Query("SELECT r FROM Reservation r WHERE r.stallId = :stallId AND " +
           "((r.startTime <= :endTime AND r.endTime >= :startTime)) AND " +
           "r.status IN ('PENDING', 'CONFIRMED')")
    List<Reservation> findConflictingReservations(@Param("stallId") UUID stallId,
                                                 @Param("startTime") LocalDateTime startTime,
                                                 @Param("endTime") LocalDateTime endTime);
}