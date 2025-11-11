package com.cibf.adminservice.admin.Repository;

import com.cibf.adminservice.admin.Entity.ReservationAnalytics;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository interface for ReservationAnalytics entity
 */
@Repository
public interface ReservationAnalyticsRepository extends JpaRepository<ReservationAnalytics, Long> {

    Optional<ReservationAnalytics> findByDate(LocalDate date);

    List<ReservationAnalytics> findByDateBetween(LocalDate startDate, LocalDate endDate);

    Page<ReservationAnalytics> findByDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);

    @Query("SELECT SUM(r.totalRevenue) FROM ReservationAnalytics r WHERE r.date BETWEEN :startDate AND :endDate")
    BigDecimal sumTotalRevenueBetweenDates(LocalDate startDate, LocalDate endDate);

    @Query("SELECT SUM(r.totalReservations) FROM ReservationAnalytics r WHERE r.date BETWEEN :startDate AND :endDate")
    Long sumTotalReservationsBetweenDates(LocalDate startDate, LocalDate endDate);

    List<ReservationAnalytics> findTop7ByOrderByDateDesc();
}

