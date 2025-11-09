package com.cibf.adminservice.admin.Controller;

import com.cibf.adminservice.admin.DTO.Internal.UserServiceDTO;
import com.cibf.adminservice.admin.DTO.Request.UpdateUserStatusRequestDTO;
import com.cibf.adminservice.admin.DTO.Response.ApiResponse;
import com.cibf.adminservice.admin.Service.UserManagementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Controller for managing users through admin service
 * Base URL: /cibf/admin-service/users-management
 */
@RestController
@RequestMapping("/users-management")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class AdminUserManagementController {

    private final UserManagementService userManagementService;

    /**
     * Get all users
     * GET /cibf/admin-service/users-management
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<UserServiceDTO>>> getAllUsers() {
        log.info("GET /users-management - Fetching all users");
        List<UserServiceDTO> users = userManagementService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.success("Users fetched successfully", users));
    }

    /**
     * Get user by ID
     * GET /cibf/admin-service/users-management/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserServiceDTO>> getUserById(@PathVariable UUID id) {
        log.info("GET /users-management/{} - Fetching user details", id);
        UserServiceDTO user = userManagementService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.success("User details fetched successfully", user));
    }

    /**
     * Update user status (enable/disable)
     * PUT /cibf/admin-service/users-management/{id}/status
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<UserServiceDTO>> updateUserStatus(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateUserStatusRequestDTO request) {
        log.info("PUT /users-management/{}/status - Updating user status to: {}", id, request.getIsActive());
        UserServiceDTO updatedUser = userManagementService.updateUserStatus(id, request.getIsActive());
        return ResponseEntity.ok(ApiResponse.success("User status updated successfully", updatedUser));
    }

    /**
     * Update user role
     * PUT /cibf/admin-service/users-management/{id}/role
     */
    @PutMapping("/{id}/role")
    public ResponseEntity<ApiResponse<UserServiceDTO>> updateUserRole(
            @PathVariable UUID id,
            @RequestParam String role) {
        log.info("PUT /users-management/{}/role - Updating user role to: {}", id, role);
        UserServiceDTO updatedUser = userManagementService.updateUserRole(id, role);
        return ResponseEntity.ok(ApiResponse.success("User role updated successfully", updatedUser));
    }

    /**
     * Search users by query
     * GET /cibf/admin-service/users-management/search
     */
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<UserServiceDTO>>> searchUsers(@RequestParam String query) {
        log.info("GET /users-management/search?query={} - Searching users", query);
        List<UserServiceDTO> users = userManagementService.searchUsers(query);
        return ResponseEntity.ok(ApiResponse.success("Search completed successfully", users));
    }

    /**
     * Get user statistics
     * GET /cibf/admin-service/users-management/statistics
     */
    @GetMapping("/statistics")
    public ResponseEntity<ApiResponse<Object>> getUserStatistics() {
        log.info("GET /users-management/statistics - Fetching user statistics");
        Object statistics = userManagementService.getUserStatistics();
        return ResponseEntity.ok(ApiResponse.success("User statistics fetched successfully", statistics));
    }
}

