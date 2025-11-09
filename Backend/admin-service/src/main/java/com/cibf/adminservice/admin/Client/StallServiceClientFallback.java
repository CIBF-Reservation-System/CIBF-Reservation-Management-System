package com.cibf.adminservice.admin.Client;

import com.cibf.adminservice.admin.DTO.Internal.StallServiceDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Fallback implementation for StallServiceClient
 * Provides default responses when stall-service is unavailable
 */
@Slf4j
@Component
public class StallServiceClientFallback implements StallServiceClient {

    @Override
    public ResponseEntity<List<StallServiceDTO>> getAllStalls() {
        log.error("Stall Service is unavailable - getAllStalls fallback triggered");
        return ResponseEntity.status(503).body(new ArrayList<>());
    }

    @Override
    public ResponseEntity<StallServiceDTO> getStallById(UUID stallId) {
        log.error("Stall Service is unavailable - getStallById fallback triggered for stallId: {}", stallId);
        return ResponseEntity.status(503).body(null);
    }

    @Override
    public ResponseEntity<List<StallServiceDTO>> getStallsByOwnerId(UUID ownerId) {
        log.error("Stall Service is unavailable - getStallsByOwnerId fallback triggered for ownerId: {}", ownerId);
        return ResponseEntity.status(503).body(new ArrayList<>());
    }

    @Override
    public ResponseEntity<List<StallServiceDTO>> getStallsByStatus(String status) {
        log.error("Stall Service is unavailable - getStallsByStatus fallback triggered for status: {}", status);
        return ResponseEntity.status(503).body(new ArrayList<>());
    }

    @Override
    public ResponseEntity<List<StallServiceDTO>> getPendingApprovalStalls() {
        log.error("Stall Service is unavailable - getPendingApprovalStalls fallback triggered");
        return ResponseEntity.status(503).body(new ArrayList<>());
    }

    @Override
    public ResponseEntity<StallServiceDTO> updateStallStatus(UUID stallId, String status) {
        log.error("Stall Service is unavailable - updateStallStatus fallback triggered for stallId: {}", stallId);
        return ResponseEntity.status(503).body(null);
    }

    @Override
    public ResponseEntity<StallServiceDTO> approveStall(UUID stallId) {
        log.error("Stall Service is unavailable - approveStall fallback triggered for stallId: {}", stallId);
        return ResponseEntity.status(503).body(null);
    }

    @Override
    public ResponseEntity<StallServiceDTO> rejectStall(UUID stallId, String reason) {
        log.error("Stall Service is unavailable - rejectStall fallback triggered for stallId: {}", stallId);
        return ResponseEntity.status(503).body(null);
    }

    @Override
    public ResponseEntity<StallServiceDTO> updateStallAvailability(UUID stallId, Boolean isAvailable) {
        log.error("Stall Service is unavailable - updateStallAvailability fallback triggered for stallId: {}", stallId);
        return ResponseEntity.status(503).body(null);
    }

    @Override
    public ResponseEntity<Object> getStallStatistics() {
        log.error("Stall Service is unavailable - getStallStatistics fallback triggered");
        return ResponseEntity.status(503).body(null);
    }

    @Override
    public ResponseEntity<List<StallServiceDTO>> searchStalls(String query) {
        log.error("Stall Service is unavailable - searchStalls fallback triggered");
        return ResponseEntity.status(503).body(new ArrayList<>());
    }

    @Override
    public ResponseEntity<Object> healthCheck() {
        log.error("Stall Service is unavailable - healthCheck fallback triggered");
        return ResponseEntity.status(503).body(null);
    }
}

