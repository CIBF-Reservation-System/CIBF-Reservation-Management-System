-- ============================================================
-- CIBF Admin Service - Database Schema DDL
-- ============================================================
-- Schema: admin-service
-- Database: MySQL 8.0+
-- Created: November 3, 2025
-- Purpose: Complete database schema for Admin Service
-- ============================================================

-- Use the admin-service schema
USE `admin-service`;

-- ============================================================
-- TABLE 1: admin_users
-- Purpose: Store admin user accounts with authentication details
-- ============================================================
CREATE TABLE IF NOT EXISTS `admin_users` (
    `admin_id` BINARY(16) NOT NULL PRIMARY KEY COMMENT 'UUID stored as binary',
    `username` VARCHAR(50) NOT NULL UNIQUE COMMENT 'Admin username for login',
    `email` VARCHAR(100) NOT NULL UNIQUE COMMENT 'Admin email address',
    `password` VARCHAR(255) NOT NULL COMMENT 'Bcrypt hashed password',
    `full_name` VARCHAR(100) NOT NULL COMMENT 'Full name of admin',
    `phone` VARCHAR(20) COMMENT 'Contact phone number',
    `role` ENUM('SUPER_ADMIN', 'ADMIN', 'MODERATOR') NOT NULL DEFAULT 'ADMIN' COMMENT 'Admin role for access control',
    `is_active` BOOLEAN NOT NULL DEFAULT TRUE COMMENT 'Account active status',
    `last_login` DATETIME(6) COMMENT 'Last successful login timestamp',
    `failed_login_attempts` INT NOT NULL DEFAULT 0 COMMENT 'Count of consecutive failed logins',
    `password_changed_at` DATETIME(6) COMMENT 'Last password change timestamp',
    `created_at` DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT 'Record creation timestamp',
    `updated_at` DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT 'Record update timestamp',
    `created_by` BINARY(16) COMMENT 'Admin ID who created this record',

    INDEX idx_email (`email`),
    INDEX idx_username (`username`),
    INDEX idx_role (`role`),
    INDEX idx_is_active (`is_active`),
    INDEX idx_created_at (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Admin user accounts';

-- ============================================================
-- TABLE 2: audit_logs
-- Purpose: System-wide audit trail for all admin actions
-- ============================================================
CREATE TABLE IF NOT EXISTS `audit_logs` (
    `log_id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'Auto-increment log ID',
    `admin_id` BINARY(16) COMMENT 'Admin who performed the action',
    `action_type` ENUM(
        'USER_CREATED', 'USER_UPDATED', 'USER_DELETED', 'USER_STATUS_CHANGED',
        'STALL_APPROVED', 'STALL_REJECTED', 'STALL_UPDATED', 'STALL_DELETED',
        'RESERVATION_CANCELLED', 'RESERVATION_UPDATED', 'RESERVATION_DELETED',
        'ADMIN_CREATED', 'ADMIN_UPDATED', 'ADMIN_DELETED', 'ADMIN_LOGIN',
        'SYSTEM_CONFIG_CHANGED', 'NOTIFICATION_SENT', 'ALERT_CREATED',
        'REPORT_GENERATED', 'OTHER'
    ) NOT NULL COMMENT 'Type of action performed',
    `entity_type` VARCHAR(50) NOT NULL COMMENT 'Type of entity affected (User, Stall, Reservation, etc.)',
    `entity_id` VARCHAR(100) COMMENT 'ID of the affected entity',
    `description` TEXT NOT NULL COMMENT 'Detailed description of the action',
    `ip_address` VARCHAR(45) COMMENT 'IP address of the admin',
    `user_agent` VARCHAR(255) COMMENT 'Browser/client user agent',
    `old_value` JSON COMMENT 'Previous value (for updates)',
    `new_value` JSON COMMENT 'New value (for updates)',
    `severity` ENUM('LOW', 'MEDIUM', 'HIGH', 'CRITICAL') NOT NULL DEFAULT 'LOW' COMMENT 'Action severity level',
    `status` ENUM('SUCCESS', 'FAILED', 'PARTIAL') NOT NULL DEFAULT 'SUCCESS' COMMENT 'Action execution status',
    `error_message` TEXT COMMENT 'Error message if action failed',
    `created_at` DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT 'Action timestamp',

    INDEX idx_admin_id (`admin_id`),
    INDEX idx_action_type (`action_type`),
    INDEX idx_entity_type (`entity_type`),
    INDEX idx_entity_id (`entity_id`),
    INDEX idx_created_at (`created_at`),
    INDEX idx_severity (`severity`),
    INDEX idx_status (`status`),
    INDEX idx_combined (`admin_id`, `action_type`, `created_at`),

    FOREIGN KEY (`admin_id`) REFERENCES `admin_users`(`admin_id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Audit trail for all admin actions';

-- ============================================================
-- TABLE 3: system_alerts
-- Purpose: System health alerts and notifications
-- ============================================================
CREATE TABLE IF NOT EXISTS `system_alerts` (
    `alert_id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'Auto-increment alert ID',
    `alert_type` ENUM(
        'SERVICE_DOWN', 'DATABASE_ERROR', 'HIGH_CPU_USAGE', 'HIGH_MEMORY_USAGE',
        'SLOW_RESPONSE', 'AUTHENTICATION_FAILURE', 'SECURITY_BREACH',
        'DATA_INCONSISTENCY', 'CUSTOM'
    ) NOT NULL COMMENT 'Type of alert',
    `severity` ENUM('LOW', 'MEDIUM', 'HIGH', 'CRITICAL') NOT NULL DEFAULT 'MEDIUM' COMMENT 'Alert severity',
    `service_name` VARCHAR(100) COMMENT 'Name of affected service',
    `title` VARCHAR(255) NOT NULL COMMENT 'Alert title/summary',
    `message` TEXT NOT NULL COMMENT 'Detailed alert message',
    `source` VARCHAR(100) COMMENT 'Source of the alert',
    `metadata` JSON COMMENT 'Additional alert metadata',
    `status` ENUM('OPEN', 'ACKNOWLEDGED', 'RESOLVED', 'CLOSED') NOT NULL DEFAULT 'OPEN' COMMENT 'Alert status',
    `acknowledged_by` BINARY(16) COMMENT 'Admin who acknowledged the alert',
    `acknowledged_at` DATETIME(6) COMMENT 'Acknowledgment timestamp',
    `resolved_by` BINARY(16) COMMENT 'Admin who resolved the alert',
    `resolved_at` DATETIME(6) COMMENT 'Resolution timestamp',
    `resolution_notes` TEXT COMMENT 'Resolution notes/comments',
    `created_at` DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT 'Alert creation timestamp',
    `updated_at` DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT 'Alert update timestamp',

    INDEX idx_alert_type (`alert_type`),
    INDEX idx_severity (`severity`),
    INDEX idx_status (`status`),
    INDEX idx_created_at (`created_at`),
    INDEX idx_service_name (`service_name`),
    INDEX idx_combined (`status`, `severity`, `created_at`),

    FOREIGN KEY (`acknowledged_by`) REFERENCES `admin_users`(`admin_id`) ON DELETE SET NULL,
    FOREIGN KEY (`resolved_by`) REFERENCES `admin_users`(`admin_id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='System health alerts and notifications';

-- ============================================================
-- TABLE 4: user_management_cache
-- Purpose: Cached user data from user-service for quick access
-- ============================================================
CREATE TABLE IF NOT EXISTS `user_management_cache` (
    `cache_id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'Auto-increment cache ID',
    `user_id` BINARY(16) NOT NULL UNIQUE COMMENT 'User ID from user-service',
    `email` VARCHAR(100) NOT NULL COMMENT 'User email',
    `business_name` VARCHAR(255) COMMENT 'Business name',
    `contact_person` VARCHAR(100) COMMENT 'Contact person name',
    `phone` VARCHAR(20) COMMENT 'Phone number',
    `role_name` VARCHAR(50) COMMENT 'User role name',
    `is_active` BOOLEAN NOT NULL DEFAULT TRUE COMMENT 'User active status',
    `total_reservations` INT NOT NULL DEFAULT 0 COMMENT 'Total number of reservations',
    `active_reservations` INT NOT NULL DEFAULT 0 COMMENT 'Number of active reservations',
    `created_at` DATETIME(6) COMMENT 'User creation date',
    `last_login` DATETIME(6) COMMENT 'Last login timestamp',
    `cached_at` DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT 'Cache timestamp',
    `updated_at` DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT 'Cache update timestamp',

    INDEX idx_user_id (`user_id`),
    INDEX idx_email (`email`),
    INDEX idx_is_active (`is_active`),
    INDEX idx_cached_at (`cached_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Cached user data for admin management';

-- ============================================================
-- TABLE 5: stall_management_cache
-- Purpose: Cached stall data from stall-service for quick access
-- ============================================================
CREATE TABLE IF NOT EXISTS `stall_management_cache` (
    `cache_id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'Auto-increment cache ID',
    `stall_id` BINARY(16) NOT NULL UNIQUE COMMENT 'Stall ID from stall-service',
    `stall_name` VARCHAR(255) NOT NULL COMMENT 'Stall name',
    `stall_number` VARCHAR(50) COMMENT 'Stall number/identifier',
    `category` VARCHAR(100) COMMENT 'Stall category',
    `owner_id` BINARY(16) COMMENT 'Owner user ID',
    `owner_name` VARCHAR(255) COMMENT 'Owner business name',
    `capacity` INT COMMENT 'Stall capacity',
    `price_per_day` DECIMAL(10, 2) COMMENT 'Daily rental price',
    `status` VARCHAR(50) COMMENT 'Stall status (AVAILABLE, BOOKED, MAINTENANCE)',
    `approval_status` VARCHAR(50) COMMENT 'Approval status (PENDING, APPROVED, REJECTED)',
    `total_bookings` INT NOT NULL DEFAULT 0 COMMENT 'Total number of bookings',
    `active_bookings` INT NOT NULL DEFAULT 0 COMMENT 'Number of active bookings',
    `created_at` DATETIME(6) COMMENT 'Stall creation date',
    `cached_at` DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT 'Cache timestamp',
    `updated_at` DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT 'Cache update timestamp',

    INDEX idx_stall_id (`stall_id`),
    INDEX idx_owner_id (`owner_id`),
    INDEX idx_status (`status`),
    INDEX idx_approval_status (`approval_status`),
    INDEX idx_category (`category`),
    INDEX idx_cached_at (`cached_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Cached stall data for admin management';

-- ============================================================
-- TABLE 6: reservation_analytics
-- Purpose: Aggregated reservation statistics and analytics
-- ============================================================
CREATE TABLE IF NOT EXISTS `reservation_analytics` (
    `analytics_id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'Auto-increment analytics ID',
    `date` DATE NOT NULL COMMENT 'Analytics date',
    `total_reservations` INT NOT NULL DEFAULT 0 COMMENT 'Total reservations on this date',
    `confirmed_reservations` INT NOT NULL DEFAULT 0 COMMENT 'Confirmed reservations',
    `cancelled_reservations` INT NOT NULL DEFAULT 0 COMMENT 'Cancelled reservations',
    `pending_reservations` INT NOT NULL DEFAULT 0 COMMENT 'Pending reservations',
    `completed_reservations` INT NOT NULL DEFAULT 0 COMMENT 'Completed reservations',
    `new_users` INT NOT NULL DEFAULT 0 COMMENT 'New user registrations',
    `new_stalls` INT NOT NULL DEFAULT 0 COMMENT 'New stall registrations',
    `total_revenue` DECIMAL(15, 2) NOT NULL DEFAULT 0.00 COMMENT 'Total revenue generated',
    `average_booking_value` DECIMAL(10, 2) NOT NULL DEFAULT 0.00 COMMENT 'Average booking value',
    `peak_booking_hour` INT COMMENT 'Hour with most bookings (0-23)',
    `most_booked_stall_id` BINARY(16) COMMENT 'Most booked stall ID',
    `most_active_user_id` BINARY(16) COMMENT 'Most active user ID',
    `created_at` DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT 'Record creation timestamp',
    `updated_at` DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT 'Record update timestamp',

    UNIQUE KEY unique_date (`date`),
    INDEX idx_date (`date`),
    INDEX idx_total_revenue (`total_revenue`),
    INDEX idx_created_at (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Daily aggregated reservation analytics';

-- ============================================================
-- TABLE 7: notification_queue
-- Purpose: Queue for pending admin notifications
-- ============================================================
CREATE TABLE IF NOT EXISTS `notification_queue` (
    `queue_id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'Auto-increment queue ID',
    `notification_type` ENUM(
        'EMAIL', 'SMS', 'PUSH', 'IN_APP'
    ) NOT NULL COMMENT 'Type of notification',
    `recipient_type` ENUM('USER', 'ADMIN', 'ALL_USERS', 'ALL_ADMINS') NOT NULL COMMENT 'Recipient type',
    `recipient_id` BINARY(16) COMMENT 'Specific recipient ID (if applicable)',
    `recipient_email` VARCHAR(100) COMMENT 'Recipient email address',
    `recipient_phone` VARCHAR(20) COMMENT 'Recipient phone number',
    `subject` VARCHAR(255) NOT NULL COMMENT 'Notification subject/title',
    `message` TEXT NOT NULL COMMENT 'Notification message body',
    `template_name` VARCHAR(100) COMMENT 'Template name (if using templates)',
    `template_variables` JSON COMMENT 'Template variables/placeholders',
    `priority` ENUM('LOW', 'MEDIUM', 'HIGH', 'URGENT') NOT NULL DEFAULT 'MEDIUM' COMMENT 'Notification priority',
    `status` ENUM('PENDING', 'PROCESSING', 'SENT', 'FAILED', 'CANCELLED') NOT NULL DEFAULT 'PENDING' COMMENT 'Queue status',
    `scheduled_at` DATETIME(6) COMMENT 'Scheduled send time (for delayed notifications)',
    `sent_at` DATETIME(6) COMMENT 'Actual send timestamp',
    `retry_count` INT NOT NULL DEFAULT 0 COMMENT 'Number of retry attempts',
    `max_retries` INT NOT NULL DEFAULT 3 COMMENT 'Maximum retry attempts',
    `error_message` TEXT COMMENT 'Error message if failed',
    `created_by` BINARY(16) COMMENT 'Admin who created the notification',
    `created_at` DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT 'Queue entry creation timestamp',
    `updated_at` DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT 'Queue entry update timestamp',

    INDEX idx_status (`status`),
    INDEX idx_notification_type (`notification_type`),
    INDEX idx_recipient_id (`recipient_id`),
    INDEX idx_priority (`priority`),
    INDEX idx_scheduled_at (`scheduled_at`),
    INDEX idx_created_at (`created_at`),
    INDEX idx_combined (`status`, `priority`, `scheduled_at`),

    FOREIGN KEY (`created_by`) REFERENCES `admin_users`(`admin_id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Notification queue for admin dispatches';

-- ============================================================
-- TABLE 8: configuration_settings
-- Purpose: System configuration key-value pairs
-- ============================================================
CREATE TABLE IF NOT EXISTS `configuration_settings` (
    `config_id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'Auto-increment config ID',
    `config_key` VARCHAR(100) NOT NULL UNIQUE COMMENT 'Configuration key (unique)',
    `config_value` TEXT NOT NULL COMMENT 'Configuration value',
    `value_type` ENUM('STRING', 'INTEGER', 'DECIMAL', 'BOOLEAN', 'JSON') NOT NULL DEFAULT 'STRING' COMMENT 'Value data type',
    `category` VARCHAR(50) NOT NULL COMMENT 'Configuration category (e.g., EMAIL, SMS, SYSTEM)',
    `description` TEXT COMMENT 'Configuration description',
    `is_encrypted` BOOLEAN NOT NULL DEFAULT FALSE COMMENT 'Whether value is encrypted',
    `is_editable` BOOLEAN NOT NULL DEFAULT TRUE COMMENT 'Whether value can be edited via UI',
    `last_modified_by` BINARY(16) COMMENT 'Admin who last modified this setting',
    `created_at` DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT 'Setting creation timestamp',
    `updated_at` DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT 'Setting update timestamp',

    INDEX idx_config_key (`config_key`),
    INDEX idx_category (`category`),
    INDEX idx_updated_at (`updated_at`),

    FOREIGN KEY (`last_modified_by`) REFERENCES `admin_users`(`admin_id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='System configuration settings';

-- ============================================================
-- TABLE 9: admin_sessions
-- Purpose: Track admin login sessions for security
-- ============================================================
CREATE TABLE IF NOT EXISTS `admin_sessions` (
    `session_id` BINARY(16) NOT NULL PRIMARY KEY COMMENT 'UUID session ID',
    `admin_id` BINARY(16) NOT NULL COMMENT 'Admin user ID',
    `jwt_token_hash` VARCHAR(255) NOT NULL COMMENT 'Hash of JWT token',
    `ip_address` VARCHAR(45) NOT NULL COMMENT 'Login IP address',
    `user_agent` VARCHAR(255) COMMENT 'Browser/client user agent',
    `device_info` JSON COMMENT 'Device information',
    `login_at` DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT 'Login timestamp',
    `last_activity_at` DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT 'Last activity timestamp',
    `expires_at` DATETIME(6) NOT NULL COMMENT 'Session expiration timestamp',
    `is_active` BOOLEAN NOT NULL DEFAULT TRUE COMMENT 'Session active status',
    `logout_at` DATETIME(6) COMMENT 'Logout timestamp',
    `logout_reason` VARCHAR(100) COMMENT 'Logout reason (manual, timeout, forced)',

    INDEX idx_admin_id (`admin_id`),
    INDEX idx_is_active (`is_active`),
    INDEX idx_expires_at (`expires_at`),
    INDEX idx_login_at (`login_at`),

    FOREIGN KEY (`admin_id`) REFERENCES `admin_users`(`admin_id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Admin user sessions for security tracking';

-- ============================================================
-- TABLE 10: system_health_metrics
-- Purpose: Store periodic system health check results
-- ============================================================
CREATE TABLE IF NOT EXISTS `system_health_metrics` (
    `metric_id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'Auto-increment metric ID',
    `check_timestamp` DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT 'Health check timestamp',
    `user_service_status` ENUM('UP', 'DOWN', 'DEGRADED', 'UNKNOWN') NOT NULL DEFAULT 'UNKNOWN' COMMENT 'User service health',
    `user_service_response_time` INT COMMENT 'User service response time (ms)',
    `stall_service_status` ENUM('UP', 'DOWN', 'DEGRADED', 'UNKNOWN') NOT NULL DEFAULT 'UNKNOWN' COMMENT 'Stall service health',
    `stall_service_response_time` INT COMMENT 'Stall service response time (ms)',
    `reservation_service_status` ENUM('UP', 'DOWN', 'DEGRADED', 'UNKNOWN') NOT NULL DEFAULT 'UNKNOWN' COMMENT 'Reservation service health',
    `reservation_service_response_time` INT COMMENT 'Reservation service response time (ms)',
    `notification_service_status` ENUM('UP', 'DOWN', 'DEGRADED', 'UNKNOWN') NOT NULL DEFAULT 'UNKNOWN' COMMENT 'Notification service health',
    `notification_service_response_time` INT COMMENT 'Notification service response time (ms)',
    `database_status` ENUM('UP', 'DOWN', 'DEGRADED', 'UNKNOWN') NOT NULL DEFAULT 'UNKNOWN' COMMENT 'Database health',
    `database_response_time` INT COMMENT 'Database response time (ms)',
    `overall_status` ENUM('HEALTHY', 'DEGRADED', 'UNHEALTHY') NOT NULL DEFAULT 'HEALTHY' COMMENT 'Overall system health',
    `cpu_usage_percent` DECIMAL(5, 2) COMMENT 'CPU usage percentage',
    `memory_usage_percent` DECIMAL(5, 2) COMMENT 'Memory usage percentage',
    `disk_usage_percent` DECIMAL(5, 2) COMMENT 'Disk usage percentage',
    `active_connections` INT COMMENT 'Number of active database connections',

    INDEX idx_check_timestamp (`check_timestamp`),
    INDEX idx_overall_status (`overall_status`),
    INDEX idx_user_service_status (`user_service_status`),
    INDEX idx_database_status (`database_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='System health metrics history';

-- ============================================================
-- INSERT DEFAULT DATA
-- ============================================================

-- Default Super Admin User (Password: Admin@123)
-- Note: This password hash is for 'Admin@123' - CHANGE IN PRODUCTION!
INSERT INTO `admin_users` (
    `admin_id`,
    `username`,
    `email`,
    `password`,
    `full_name`,
    `phone`,
    `role`,
    `is_active`,
    `created_at`,
    `updated_at`
) VALUES (
    UNHEX(REPLACE(UUID(), '-', '')),
    'superadmin',
    'superadmin@cibf.lk',
    '$2a$10$XYZ123...', -- Replace with actual bcrypt hash
    'Super Administrator',
    '+94771234567',
    'SUPER_ADMIN',
    TRUE,
    CURRENT_TIMESTAMP(6),
    CURRENT_TIMESTAMP(6)
) ON DUPLICATE KEY UPDATE `admin_id` = `admin_id`;

-- Default Configuration Settings
INSERT INTO `configuration_settings` (`config_key`, `config_value`, `value_type`, `category`, `description`, `is_editable`) VALUES
('system.name', 'CIBF Admin Service', 'STRING', 'SYSTEM', 'System name', TRUE),
('system.version', '1.0.0', 'STRING', 'SYSTEM', 'System version', FALSE),
('system.maintenance_mode', 'false', 'BOOLEAN', 'SYSTEM', 'Enable/disable maintenance mode', TRUE),
('session.timeout_minutes', '30', 'INTEGER', 'SECURITY', 'Session timeout in minutes', TRUE),
('session.max_concurrent_sessions', '3', 'INTEGER', 'SECURITY', 'Maximum concurrent sessions per admin', TRUE),
('password.min_length', '8', 'INTEGER', 'SECURITY', 'Minimum password length', TRUE),
('password.require_special_char', 'true', 'BOOLEAN', 'SECURITY', 'Require special character in password', TRUE),
('password.expiry_days', '90', 'INTEGER', 'SECURITY', 'Password expiry in days', TRUE),
('email.from_address', 'noreply@cibf.lk', 'STRING', 'EMAIL', 'Default from email address', TRUE),
('email.from_name', 'CIBF Admin', 'STRING', 'EMAIL', 'Default from name', TRUE),
('notification.retry_max', '3', 'INTEGER', 'NOTIFICATION', 'Maximum notification retry attempts', TRUE),
('notification.retry_delay_seconds', '60', 'INTEGER', 'NOTIFICATION', 'Delay between retries in seconds', TRUE),
('analytics.cache_duration_minutes', '15', 'INTEGER', 'ANALYTICS', 'Analytics data cache duration', TRUE),
('health_check.interval_seconds', '60', 'INTEGER', 'MONITORING', 'Health check interval in seconds', TRUE),
('alert.auto_resolve_hours', '24', 'INTEGER', 'MONITORING', 'Auto-resolve alerts after hours', TRUE)
ON DUPLICATE KEY UPDATE `config_id` = `config_id`;

-- ============================================================
-- VIEWS FOR EASIER DATA ACCESS
-- ============================================================

-- View: Active Admin Users
CREATE OR REPLACE VIEW `v_active_admins` AS
SELECT
    HEX(`admin_id`) AS `admin_id_hex`,
    `username`,
    `email`,
    `full_name`,
    `phone`,
    `role`,
    `last_login`,
    `created_at`
FROM `admin_users`
WHERE `is_active` = TRUE
ORDER BY `created_at` DESC;

-- View: Recent Audit Logs (Last 7 Days)
CREATE OR REPLACE VIEW `v_recent_audit_logs` AS
SELECT
    `log_id`,
    HEX(`admin_id`) AS `admin_id_hex`,
    `action_type`,
    `entity_type`,
    `entity_id`,
    `description`,
    `severity`,
    `status`,
    `created_at`
FROM `audit_logs`
WHERE `created_at` >= DATE_SUB(NOW(), INTERVAL 7 DAY)
ORDER BY `created_at` DESC;

-- View: Open System Alerts
CREATE OR REPLACE VIEW `v_open_alerts` AS
SELECT
    `alert_id`,
    `alert_type`,
    `severity`,
    `service_name`,
    `title`,
    `message`,
    `status`,
    `created_at`
FROM `system_alerts`
WHERE `status` IN ('OPEN', 'ACKNOWLEDGED')
ORDER BY `severity` DESC, `created_at` DESC;

-- View: Pending Notifications
CREATE OR REPLACE VIEW `v_pending_notifications` AS
SELECT
    `queue_id`,
    `notification_type`,
    `recipient_type`,
    `recipient_email`,
    `subject`,
    `priority`,
    `status`,
    `scheduled_at`,
    `retry_count`,
    `created_at`
FROM `notification_queue`
WHERE `status` = 'PENDING'
ORDER BY `priority` DESC, `scheduled_at` ASC;

-- ============================================================
-- STORED PROCEDURES
-- ============================================================

-- Procedure: Clean Old Audit Logs (older than 90 days)
DELIMITER $$
CREATE PROCEDURE `sp_cleanup_old_audit_logs`()
BEGIN
    DELETE FROM `audit_logs`
    WHERE `created_at` < DATE_SUB(NOW(), INTERVAL 90 DAY);

    SELECT ROW_COUNT() AS deleted_count;
END$$
DELIMITER ;

-- Procedure: Auto-resolve old alerts (older than 24 hours)
DELIMITER $$
CREATE PROCEDURE `sp_auto_resolve_old_alerts`()
BEGIN
    UPDATE `system_alerts`
    SET
        `status` = 'CLOSED',
        `resolution_notes` = 'Auto-resolved by system after 24 hours',
        `resolved_at` = NOW()
    WHERE
        `status` = 'OPEN'
        AND `created_at` < DATE_SUB(NOW(), INTERVAL 24 HOUR);

    SELECT ROW_COUNT() AS resolved_count;
END$$
DELIMITER ;

-- Procedure: Cleanup expired sessions
DELIMITER $$
CREATE PROCEDURE `sp_cleanup_expired_sessions`()
BEGIN
    UPDATE `admin_sessions`
    SET
        `is_active` = FALSE,
        `logout_reason` = 'expired'
    WHERE
        `is_active` = TRUE
        AND `expires_at` < NOW();

    SELECT ROW_COUNT() AS expired_count;
END$$
DELIMITER ;

-- ============================================================
-- EVENTS (Automated Tasks)
-- ============================================================

-- Enable Event Scheduler
SET GLOBAL event_scheduler = ON;

-- Event: Daily cleanup of old audit logs
CREATE EVENT IF NOT EXISTS `evt_daily_audit_cleanup`
ON SCHEDULE EVERY 1 DAY
STARTS (TIMESTAMP(CURRENT_DATE) + INTERVAL 1 DAY + INTERVAL 2 HOUR)
DO
    CALL `sp_cleanup_old_audit_logs`();

-- Event: Hourly auto-resolve old alerts
CREATE EVENT IF NOT EXISTS `evt_hourly_alert_resolve`
ON SCHEDULE EVERY 1 HOUR
STARTS (CURRENT_TIMESTAMP + INTERVAL 1 HOUR)
DO
    CALL `sp_auto_resolve_old_alerts`();

-- Event: Every 15 minutes cleanup expired sessions
CREATE EVENT IF NOT EXISTS `evt_session_cleanup`
ON SCHEDULE EVERY 15 MINUTE
STARTS CURRENT_TIMESTAMP
DO
    CALL `sp_cleanup_expired_sessions`();

-- ============================================================
-- VERIFICATION QUERIES
-- ============================================================

-- Verify all tables created
SELECT
    TABLE_NAME,
    TABLE_ROWS,
    CREATE_TIME,
    TABLE_COMMENT
FROM information_schema.TABLES
WHERE TABLE_SCHEMA = 'admin-service'
ORDER BY TABLE_NAME;

-- Verify indexes
SELECT
    TABLE_NAME,
    INDEX_NAME,
    GROUP_CONCAT(COLUMN_NAME ORDER BY SEQ_IN_INDEX) AS COLUMNS
FROM information_schema.STATISTICS
WHERE TABLE_SCHEMA = 'admin-service'
GROUP BY TABLE_NAME, INDEX_NAME
ORDER BY TABLE_NAME, INDEX_NAME;

-- ============================================================
-- COMPLETION MESSAGE
-- ============================================================
SELECT
    'Database schema created successfully!' AS message,
    (SELECT COUNT(*) FROM information_schema.TABLES WHERE TABLE_SCHEMA = 'admin-service') AS total_tables,
    (SELECT COUNT(*) FROM information_schema.VIEWS WHERE TABLE_SCHEMA = 'admin-service') AS total_views,
    NOW() AS created_at;

-- ============================================================
-- END OF DDL SCRIPT
-- ============================================================

