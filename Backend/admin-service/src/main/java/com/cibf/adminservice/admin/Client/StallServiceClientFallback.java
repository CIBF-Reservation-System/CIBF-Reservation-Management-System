package com.cibf.adminservice.admin.Client;

import com.cibf.adminservice.admin.DTO.Internal.StallServiceDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class StallServiceClientFallback implements StallServiceClient {

    @Override
    public ResponseEntity<List<StallServiceDTO>> getAllStalls() {
        log.error("Stall Service unavailable - getAllStalls fallback");
        return ResponseEntity.status(503).body(new ArrayList<>());
    }

    @Override
    public ResponseEntity<StallServiceDTO> getStallById(UUID stallId) {
        log.error("Stall Service unavailable - getStallById fallback for stallId {}", stallId);
        return ResponseEntity.status(503).body(null);
    }

    @Override
    public ResponseEntity<List<StallServiceDTO>> getAvailableStalls() {
        log.error("Stall Service unavailable - getAvailableStalls fallback");
        return ResponseEntity.status(503).body(new ArrayList<>());
    }

    @Override
    public ResponseEntity<List<StallServiceDTO>> getReservedStalls() {
        log.error("Stall Service unavailable - getReservedStalls fallback");
        return ResponseEntity.status(503).body(new ArrayList<>());
    }

    @Override
    public ResponseEntity<StallServiceDTO> updateStallAvailability(UUID stallId) {
        log.error("Stall Service unavailable - updateStallAvailability fallback for stallId {}", stallId);
        return ResponseEntity.status(503).body(null);
    }

    @Override
    public ResponseEntity<StallServiceDTO> saveStall(StallServiceDTO stallDTO) {
        log.error("Stall Service unavailable - saveStall fallback");
        return ResponseEntity.status(503).body(null);
    }

    @Override
    public ResponseEntity<StallServiceDTO> updateStall(UUID stallId, StallServiceDTO stallDTO) {
        log.error("Stall Service unavailable - updateStall fallback for stallId {}", stallId);
        return ResponseEntity.status(503).body(null);
    }

    @Override
    public ResponseEntity<Void> deleteStall(UUID stallId) {
        log.error("Stall Service unavailable - deleteStall fallback for stallId {}", stallId);
        return ResponseEntity.status(503).build();
    }

    @Override
    public ResponseEntity<List<StallServiceDTO>> getPendingApprovalStalls() {
        log.error("Stall Service unavailable - getPendingApprovalStalls fallback");
        return ResponseEntity.status(503).body(new ArrayList<>());
    }

    @Override
    public ResponseEntity<StallServiceDTO> updateStallAvailability(UUID stallId, Boolean isAvailable) {
        log.error("Stall Service is unavailable - updateStallAvailability fallback for stallId: {}", stallId);
        return ResponseEntity.status(503).body(null);
    }

}
