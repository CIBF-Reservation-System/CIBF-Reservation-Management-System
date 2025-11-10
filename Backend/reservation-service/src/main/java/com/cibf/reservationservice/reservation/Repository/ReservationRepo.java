package com.cibf.reservationservice.reservation.Repository;


import com.cibf.reservationservice.reservation.Entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationRepo extends JpaRepository<Reservation,Integer> {
    @Query(value = "SELECT * FROM reservation WHERE id = ?1", nativeQuery = true)
    Reservation getReservationById(Integer reserveId);
}
