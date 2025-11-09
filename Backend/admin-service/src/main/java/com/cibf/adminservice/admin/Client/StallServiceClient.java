package com.cibf.adminservice.admin.Client;

import com.cibf.adminservice.admin.DTO.Internal.StallServiceDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Feign Client for Stall Service
 * Communicates with stall-service to fetch and manage stall data
 */
@FeignClient(
        name = "stall-service",
        url = "${spring.cloud.openfeign.client.config.stall-service.url}",
        fallback = StallServiceClientFallback.class
)
public interface StallServiceClient {

    /**
     * Get all stalls from stall service
     */
    @GetMapping("/api/v1/stalls")
    ResponseEntity<List<StallServiceDTO>> getAllStalls();

    /**
     * Get stall by ID
     */
    @GetMapping("/api/v1/stalls/{stallId}")
    ResponseEntity<StallServiceDTO> getStallById(@PathVariable("stallId") UUID stallId);

    /**
     * Get stalls by owner ID
     */
    @GetMapping("/api/v1/stalls/owner/{ownerId}")
    ResponseEntity<List<StallServiceDTO>> getStallsByOwnerId(@PathVariable("ownerId") UUID ownerId);

    /**
     * Get stalls by status
     */
    @GetMapping("/api/v1/stalls/status/{status}")
    ResponseEntity<List<StallServiceDTO>> getStallsByStatus(@PathVariable("status") String status);

    /**
     * Get pending approval stalls
     */
    @GetMapping("/api/v1/stalls/pending-approval")
    ResponseEntity<List<StallServiceDTO>> getPendingApprovalStalls();

    /**
     * Update stall status
     */
    @PatchMapping("/api/v1/stalls/{stallId}/status")
    ResponseEntity<StallServiceDTO> updateStallStatus(
            @PathVariable("stallId") UUID stallId,
            @RequestParam("status") String status
    );

    /**
     * Approve stall
     */
    @PatchMapping("/api/v1/stalls/{stallId}/approve")
    ResponseEntity<StallServiceDTO> approveStall(@PathVariable("stallId") UUID stallId);

    /**
     * Reject stall
     */
    @PatchMapping("/api/v1/stalls/{stallId}/reject")
    ResponseEntity<StallServiceDTO> rejectStall(
            @PathVariable("stallId") UUID stallId,
            @RequestParam("reason") String reason
    );

    /**
     * Update stall availability
     */
    @PatchMapping("/api/v1/stalls/{stallId}/availability")
    ResponseEntity<StallServiceDTO> updateStallAvailability(
            @PathVariable("stallId") UUID stallId,
            @RequestParam("isAvailable") Boolean isAvailable
    );

    /**
     * Get stall statistics
     */
    @GetMapping("/api/v1/stalls/statistics")
    ResponseEntity<Object> getStallStatistics();

    /**
     * Search stalls by name or category
     */
    @GetMapping("/api/v1/stalls/search")
    ResponseEntity<List<StallServiceDTO>> searchStalls(@RequestParam("query") String query);
}

