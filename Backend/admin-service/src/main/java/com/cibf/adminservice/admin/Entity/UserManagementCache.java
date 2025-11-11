package com.cibf.adminservice.admin.Entity;

import com.cibf.adminservice.admin.Common.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity class for user_management_cache table
 * Cached user data from user-service for quick access
 */
@Entity
@Table(name = "user_management_cache", indexes = {
        @Index(name = "idx_user_id", columnList = "user_id"),
        @Index(name = "idx_email", columnList = "email"),
        @Index(name = "idx_is_active", columnList = "is_active"),
        @Index(name = "idx_cached_at", columnList = "cached_at")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserManagementCache extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cache_id")
    private Long cacheId;

    @Column(name = "user_id", columnDefinition = "BINARY(16)", nullable = false, unique = true)
    private UUID userId;

    @Column(name = "email", nullable = false, length = 100)
    private String email;

    @Column(name = "business_name", length = 255)
    private String businessName;

    @Column(name = "contact_person", length = 100)
    private String contactPerson;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "role_name", length = 50)
    private String roleName;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "total_reservations", nullable = false)
    private Integer totalReservations = 0;

    @Column(name = "active_reservations", nullable = false)
    private Integer activeReservations = 0;

    @Column(name = "created_at_user")
    private LocalDateTime createdAtUser;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @Column(name = "cached_at", nullable = false)
    private LocalDateTime cachedAt = LocalDateTime.now();
}

