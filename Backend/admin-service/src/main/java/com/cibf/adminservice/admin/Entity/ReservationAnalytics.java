package com.cibf.adminservice.admin.Entity;

import com.cibf.adminservice.admin.Common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

/**
 * Entity class for reservation_analytics table
 * Aggregated reservation statistics and analytics
 */
@Entity
@Table(name = "reservation_analytics", indexes = {
        @Index(name = "idx_date", columnList = "date"),
        @Index(name = "idx_total_revenue", columnList = "total_revenue"),
        @Index(name = "idx_created_at", columnList = "created_at")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ReservationAnalytics extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "analytics_id")
    private Long analyticsId;

    @Column(name = "date", nullable = false, unique = true)
    private LocalDate date;

    @Column(name = "total_reservations", nullable = false)
    private Integer totalReservations = 0;

    @Column(name = "confirmed_reservations", nullable = false)
    private Integer confirmedReservations = 0;

    @Column(name = "cancelled_reservations", nullable = false)
    private Integer cancelledReservations = 0;

    @Column(name = "pending_reservations", nullable = false)
    private Integer pendingReservations = 0;

    @Column(name = "completed_reservations", nullable = false)
    private Integer completedReservations = 0;

    @Column(name = "new_users", nullable = false)
    private Integer newUsers = 0;

    @Column(name = "new_stalls", nullable = false)
    private Integer newStalls = 0;

    @Column(name = "total_revenue", nullable = false, precision = 15, scale = 2)
    private BigDecimal totalRevenue = BigDecimal.ZERO;

    @Column(name = "average_booking_value", nullable = false, precision = 10, scale = 2)
    private BigDecimal averageBookingValue = BigDecimal.ZERO;

    @Column(name = "peak_booking_hour")
    private Integer peakBookingHour;

    @Column(name = "most_booked_stall_id", columnDefinition = "BINARY(16)")
    private UUID mostBookedStallId;

    @Column(name = "most_active_user_id", columnDefinition = "BINARY(16)")
    private UUID mostActiveUserId;
}

