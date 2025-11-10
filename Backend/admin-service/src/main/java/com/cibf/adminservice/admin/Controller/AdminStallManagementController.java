package com.cibf.adminservice.admin.Controller;

import com.cibf.adminservice.admin.DTO.Internal.StallServiceDTO;
import com.cibf.adminservice.admin.DTO.Request.StallApprovalRequestDTO;
import com.cibf.adminservice.admin.DTO.Response.ApiResponse;
import com.cibf.adminservice.admin.Service.StallManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Controller for managing stalls through admin service
 * Base URL: /cibf/admin-service/stalls-management
 */
@RestController
@RequestMapping("/stalls-management")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
@Tag(name = "Stall Management", description = "Admin endpoints for managing book fair stalls")
public class AdminStallManagementController {

    private final StallManagementService stallManagementService;

    /**
     * Get all stalls
     * GET /cibf/admin-service/stalls-management
     */
    @GetMapping
    @Operation(summary = "Get all stalls", description = "Retrieve all stalls in the system")
    public ResponseEntity<ApiResponse<List<StallServiceDTO>>> getAllStalls() {
        log.info("GET /stalls-management - Fetching all stalls");
        List<StallServiceDTO> stalls = stallManagementService.getAllStalls();
        return ResponseEntity.ok(ApiResponse.success("Stalls fetched successfully", stalls));
    }

    /**
     * Get stall by ID
     * GET /cibf/admin-service/stalls-management/{id}
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get stall by ID", description = "Retrieve specific stall details by stall ID")
    public ResponseEntity<ApiResponse<StallServiceDTO>> getStallById(
            @PathVariable @Parameter(description = "Stall ID") UUID id) {
        log.info("GET /stalls-management/{} - Fetching stall details", id);
        StallServiceDTO stall = stallManagementService.getStallById(id);
        return ResponseEntity.ok(ApiResponse.success("Stall details fetched successfully", stall));
    }

    /**
     * Get stalls by status
     * GET /cibf/admin-service/stalls-management/status/{status}
     */
    @GetMapping("/status/{status}")
    @Operation(summary = "Get stalls by status", description = "Retrieve stalls filtered by status")
    public ResponseEntity<ApiResponse<List<StallServiceDTO>>> getStallsByStatus(
            @PathVariable @Parameter(description = "Stall status") String status) {
        log.info("GET /stalls-management/status/{} - Fetching stalls by status", status);
        List<StallServiceDTO> stalls = stallManagementService.getStallsByStatus(status);
        return ResponseEntity.ok(ApiResponse.success("Stalls fetched successfully", stalls));
    }

    /**
     * Get pending approval stalls
     * GET /cibf/admin-service/stalls-management/pending-approval
     */
    @GetMapping("/pending-approval")
    @Operation(summary = "Get pending approval stalls", description = "Retrieve stalls awaiting approval")
    public ResponseEntity<ApiResponse<List<StallServiceDTO>>> getPendingApprovalStalls() {
        log.info("GET /stalls-management/pending-approval - Fetching pending stalls");
        List<StallServiceDTO> stalls = stallManagementService.getPendingApprovalStalls();
        return ResponseEntity.ok(ApiResponse.success("Pending stalls fetched successfully", stalls));
    }

    /**
     * Approve stall
     * PUT /cibf/admin-service/stalls-management/{id}/approve
     */
    @PutMapping("/{id}/approve")
    @Operation(summary = "Approve stall", description = "Approve a stall application")
    public ResponseEntity<ApiResponse<StallServiceDTO>> approveStall(
            @PathVariable @Parameter(description = "Stall ID") UUID id) {
        log.info("PUT /stalls-management/{}/approve - Approving stall", id);
        StallServiceDTO approvedStall = stallManagementService.approveStall(id);
        return ResponseEntity.ok(ApiResponse.success("Stall approved successfully", approvedStall));
    }

    /**
     * Reject stall
     * PUT /cibf/admin-service/stalls-management/{id}/reject
     */
    @PutMapping("/{id}/reject")
    @Operation(summary = "Reject stall", description = "Reject a stall application with remarks")
    public ResponseEntity<ApiResponse<StallServiceDTO>> rejectStall(
            @PathVariable @Parameter(description = "Stall ID") UUID id,
            @Valid @RequestBody StallApprovalRequestDTO request) {
        log.info("PUT /stalls-management/{}/reject - Rejecting stall with reason: {}", id, request.getRemarks());
        StallServiceDTO rejectedStall = stallManagementService.rejectStall(id, request.getRemarks());
        return ResponseEntity.ok(ApiResponse.success("Stall rejected successfully", rejectedStall));
    }

    /**
     * Update stall status
     * PUT /cibf/admin-service/stalls-management/{id}/status
     */
    @PutMapping("/{id}/status")
    @Operation(summary = "Update stall status", description = "Update the status of a stall")
    public ResponseEntity<ApiResponse<StallServiceDTO>> updateStallStatus(
            @PathVariable @Parameter(description = "Stall ID") UUID id,
            @RequestParam @Parameter(description = "New status") String status) {
        log.info("PUT /stalls-management/{}/status - Updating stall status to: {}", id, status);
        StallServiceDTO updatedStall = stallManagementService.updateStallStatus(id, status);
        return ResponseEntity.ok(ApiResponse.success("Stall status updated successfully", updatedStall));
    }

    /**
     * Update stall availability
     * PUT /cibf/admin-service/stalls-management/{id}/availability
     */
    @PutMapping("/{id}/availability")
    @Operation(summary = "Update stall availability", description = "Update stall availability for booking")
    public ResponseEntity<ApiResponse<StallServiceDTO>> updateStallAvailability(
            @PathVariable @Parameter(description = "Stall ID") UUID id,
            @RequestParam @Parameter(description = "Availability status") Boolean isAvailable) {
        log.info("PUT /stalls-management/{}/availability - Updating availability to: {}", id, isAvailable);
        StallServiceDTO updatedStall = stallManagementService.updateStallAvailability(id, isAvailable);
        return ResponseEntity.ok(ApiResponse.success("Stall availability updated successfully", updatedStall));
    }

    /**
     * Search stalls by query
     * GET /cibf/admin-service/stalls-management/search
     */
    @GetMapping("/search")
    @Operation(summary = "Search stalls", description = "Search stalls by name, description, or category")
    public ResponseEntity<ApiResponse<List<StallServiceDTO>>> searchStalls(
            @RequestParam @Parameter(description = "Search query") String query) {
        log.info("GET /stalls-management/search?query={} - Searching stalls", query);
        List<StallServiceDTO> stalls = stallManagementService.searchStalls(query);
        return ResponseEntity.ok(ApiResponse.success("Search completed successfully", stalls));
    }

    /**
     * Get stall statistics
     * GET /cibf/admin-service/stalls-management/statistics
     */
    @GetMapping("/statistics")
    @Operation(summary = "Get stall statistics", description = "Retrieve stall statistics and metrics")
    public ResponseEntity<ApiResponse<Object>> getStallStatistics() {
        log.info("GET /stalls-management/statistics - Fetching stall statistics");
        Object statistics = stallManagementService.getStallStatistics();
        return ResponseEntity.ok(ApiResponse.success("Stall statistics fetched successfully", statistics));
    }
}

