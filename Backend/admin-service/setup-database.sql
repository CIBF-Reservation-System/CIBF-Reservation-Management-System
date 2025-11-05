-- Admin Service Database Manual Setup (if needed)
-- Run this only if automatic database creation fails

-- Create database
CREATE DATABASE IF NOT EXISTS admin_service_db;

-- Use the database
USE admin_service_db;

-- Create health_checks table
CREATE TABLE IF NOT EXISTS health_checks (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    service_name VARCHAR(255) NOT NULL,
    status VARCHAR(255) NOT NULL,
    message VARCHAR(255),
    checked_at DATETIME(6)
);

-- Verify table creation
DESCRIBE health_checks;

-- Show tables
SHOW TABLES;

SELECT 'Database setup completed successfully!' AS message;

