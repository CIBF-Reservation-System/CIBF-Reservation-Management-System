package com.cibf.adminservice.admin.Service;

import com.cibf.adminservice.admin.Client.StallServiceClient;
import com.cibf.adminservice.admin.DTO.Internal.StallServiceDTO;
import com.cibf.adminservice.admin.Exception.ServiceUnavailableException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Service for managing stall-related operations via stall-service
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class StallManagementService {

    private final StallServiceClient stallServiceClient;

    /**
     * Get all stalls
     */
    public List<StallServiceDTO> getAllStalls() {
        log.info("Fetching all stalls from stall-service");
        try {
            ResponseEntity<List<StallServiceDTO>> response = stallServiceClient.getAllStalls();
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                log.info("Successfully fetched {} stalls", response.getBody().size());
                return response.getBody();
            }
            throw new ServiceUnavailableException("Stall service returned empty response");
        } catch (Exception e) {
            log.error("Error fetching stalls: {}", e.getMessage());
            throw new ServiceUnavailableException("Stall service is currently unavailable");
        }
    }

    /**
     * Get stall by ID
     */
    public StallServiceDTO getStallById(UUID stallId) {
        log.info("Fetching stall with ID: {}", stallId);
        try {
            ResponseEntity<StallServiceDTO> response = stallServiceClient.getStallById(stallId);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                log.info("Successfully fetched stall: {}", response.getBody().getStallName());
                return response.getBody();
            }
            throw new ServiceUnavailableException("Stall not found or service unavailable");
        } catch (Exception e) {
            log.error("Error fetching stall {}: {}", stallId, e.getMessage());
            throw new ServiceUnavailableException("Stall service is currently unavailable");
        }
    }

    /**
     * Get stalls by status
     */
    public List<StallServiceDTO> getStallsByStatus(String status) {
        log.info("Fetching stalls with status: {}", status);
        try {
            ResponseEntity<List<StallServiceDTO>> response = stallServiceClient.getStallsByStatus(status);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                log.info("Found {} stalls with status {}", response.getBody().size(), status);
                return response.getBody();
            }
            throw new ServiceUnavailableException("Failed to fetch stalls by status");
        } catch (Exception e) {
            log.error("Error fetching stalls by status: {}", e.getMessage());
            throw new ServiceUnavailableException("Stall service is currently unavailable");
        }
    }

    /**
     * Get pending approval stalls
     */
    public List<StallServiceDTO> getPendingApprovalStalls() {
        log.info("Fetching pending approval stalls");
        try {
            ResponseEntity<List<StallServiceDTO>> response = stallServiceClient.getPendingApprovalStalls();
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                log.info("Found {} pending stalls", response.getBody().size());
                return response.getBody();
            }
            throw new ServiceUnavailableException("Failed to fetch pending stalls");
        } catch (Exception e) {
            log.error("Error fetching pending stalls: {}", e.getMessage());
            throw new ServiceUnavailableException("Stall service is currently unavailable");
        }
    }

    /**
     * Approve stall
     */
    public StallServiceDTO approveStall(UUID stallId) {
        log.info("Approving stall: {}", stallId);
        try {
            ResponseEntity<StallServiceDTO> response = stallServiceClient.approveStall(stallId);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                log.info("Successfully approved stall");
                return response.getBody();
            }
            throw new ServiceUnavailableException("Failed to approve stall");
        } catch (Exception e) {
            log.error("Error approving stall: {}", e.getMessage());
            throw new ServiceUnavailableException("Stall service is currently unavailable");
        }
    }

    /**
     * Reject stall
     */
    public StallServiceDTO rejectStall(UUID stallId, String reason) {
        log.info("Rejecting stall: {} with reason: {}", stallId, reason);
        try {
            ResponseEntity<StallServiceDTO> response = stallServiceClient.rejectStall(stallId, reason);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                log.info("Successfully rejected stall");
                return response.getBody();
            }
            throw new ServiceUnavailableException("Failed to reject stall");
        } catch (Exception e) {
            log.error("Error rejecting stall: {}", e.getMessage());
            throw new ServiceUnavailableException("Stall service is currently unavailable");
        }
    }

    /**
     * Update stall status
     */
    public StallServiceDTO updateStallStatus(UUID stallId, String status) {
        log.info("Updating stall {} status to: {}", stallId, status);
        try {
            ResponseEntity<StallServiceDTO> response = stallServiceClient.updateStallStatus(stallId, status);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                log.info("Successfully updated stall status");
                return response.getBody();
            }
            throw new ServiceUnavailableException("Failed to update stall status");
        } catch (Exception e) {
            log.error("Error updating stall status: {}", e.getMessage());
            throw new ServiceUnavailableException("Stall service is currently unavailable");
        }
    }

    /**
     * Update stall availability
     */
    public StallServiceDTO updateStallAvailability(UUID stallId, Boolean isAvailable) {
        log.info("Updating stall {} availability to: {}", stallId, isAvailable);
        try {
            ResponseEntity<StallServiceDTO> response = stallServiceClient.updateStallAvailability(stallId, isAvailable);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                log.info("Successfully updated stall availability");
                return response.getBody();
            }
            throw new ServiceUnavailableException("Failed to update stall availability");
        } catch (Exception e) {
            log.error("Error updating stall availability: {}", e.getMessage());
            throw new ServiceUnavailableException("Stall service is currently unavailable");
        }
    }

    /**
     * Search stalls
     */
    public List<StallServiceDTO> searchStalls(String query) {
        log.info("Searching stalls with query: {}", query);
        try {
            ResponseEntity<List<StallServiceDTO>> response = stallServiceClient.searchStalls(query);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                log.info("Found {} stalls matching query", response.getBody().size());
                return response.getBody();
            }
            throw new ServiceUnavailableException("Search failed");
        } catch (Exception e) {
            log.error("Error searching stalls: {}", e.getMessage());
            throw new ServiceUnavailableException("Stall service is currently unavailable");
        }
    }

    /**
     * Get stall statistics
     */
    public Object getStallStatistics() {
        log.info("Fetching stall statistics");
        try {
            ResponseEntity<Object> response = stallServiceClient.getStallStatistics();
            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Successfully fetched stall statistics");
                return response.getBody();
            }
            throw new ServiceUnavailableException("Failed to fetch statistics");
        } catch (Exception e) {
            log.error("Error fetching stall statistics: {}", e.getMessage());
            throw new ServiceUnavailableException("Stall service is currently unavailable");
        }
    }
}

