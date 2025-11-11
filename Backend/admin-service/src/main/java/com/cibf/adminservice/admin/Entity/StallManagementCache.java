package com.cibf.adminservice.admin.Entity;

import com.cibf.adminservice.admin.Common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity class for stall_management_cache table
 * Cached stall data from stall-service for quick access
 */
@Entity
@Table(name = "stall_management_cache", indexes = {
        @Index(name = "idx_stall_id", columnList = "stall_id"),
        @Index(name = "idx_owner_id", columnList = "owner_id"),
        @Index(name = "idx_status", columnList = "status"),
        @Index(name = "idx_approval_status", columnList = "approval_status"),
        @Index(name = "idx_category", columnList = "category"),
        @Index(name = "idx_cached_at", columnList = "cached_at")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class StallManagementCache extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cache_id")
    private Long cacheId;

    @Column(name = "stall_id", columnDefinition = "BINARY(16)", nullable = false, unique = true)
    private UUID stallId;

    @Column(name = "stall_name", nullable = false, length = 255)
    private String stallName;

    @Column(name = "stall_number", length = 50)
    private String stallNumber;

    @Column(name = "category", length = 100)
    private String category;

    @Column(name = "owner_id", columnDefinition = "BINARY(16)")
    private UUID ownerId;

    @Column(name = "owner_name", length = 255)
    private String ownerName;

    @Column(name = "capacity")
    private Integer capacity;

    @Column(name = "price_per_day", precision = 10, scale = 2)
    private BigDecimal pricePerDay;

    @Column(name = "status", length = 50)
    private String status;

    @Column(name = "approval_status", length = 50)
    private String approvalStatus;

    @Column(name = "total_bookings", nullable = false)
    private Integer totalBookings = 0;

    @Column(name = "active_bookings", nullable = false)
    private Integer activeBookings = 0;

    @Column(name = "created_at_stall")
    private LocalDateTime createdAtStall;

    @Column(name = "cached_at", nullable = false)
    private LocalDateTime cachedAt = LocalDateTime.now();
}

