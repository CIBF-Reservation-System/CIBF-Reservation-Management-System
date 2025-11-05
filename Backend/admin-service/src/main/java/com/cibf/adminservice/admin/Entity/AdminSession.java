package com.cibf.adminservice.admin.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Entity class for admin_sessions table
 * Track admin login sessions for security
 */
@Entity
@Table(name = "admin_sessions", indexes = {
        @Index(name = "idx_admin_id", columnList = "admin_id"),
        @Index(name = "idx_is_active", columnList = "is_active"),
        @Index(name = "idx_expires_at", columnList = "expires_at"),
        @Index(name = "idx_login_at", columnList = "login_at")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminSession {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "session_id", columnDefinition = "BINARY(16)", nullable = false)
    private UUID sessionId;

    @Column(name = "admin_id", columnDefinition = "BINARY(16)", nullable = false)
    private UUID adminId;

    @Column(name = "jwt_token_hash", nullable = false, length = 255)
    private String jwtTokenHash;

    @Column(name = "ip_address", nullable = false, length = 45)
    private String ipAddress;

    @Column(name = "user_agent", length = 255)
    private String userAgent;

    @Column(name = "device_info", columnDefinition = "JSON")
    private String deviceInfo;

    @CreationTimestamp
    @Column(name = "login_at", nullable = false, updatable = false)
    private LocalDateTime loginAt;

    @Column(name = "last_activity_at", nullable = false)
    private LocalDateTime lastActivityAt = LocalDateTime.now();

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "logout_at")
    private LocalDateTime logoutAt;

    @Column(name = "logout_reason", length = 100)
    private String logoutReason;
}

