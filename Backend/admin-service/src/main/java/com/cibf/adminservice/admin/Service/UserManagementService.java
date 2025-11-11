package com.cibf.adminservice.admin.Service;

import com.cibf.adminservice.admin.Client.UserServiceClient;
import com.cibf.adminservice.admin.DTO.Internal.UserServiceDTO;
import com.cibf.adminservice.admin.Exception.ServiceUnavailableException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Service for managing user-related operations via user-service
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class UserManagementService {

    private final UserServiceClient userServiceClient;

    /**
     * Get all users
     */
    public List<UserServiceDTO> getAllUsers() {
        log.info("Fetching all users from user-service");
        try {
            ResponseEntity<List<UserServiceDTO>> response = userServiceClient.getAllUsers();
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                log.info("Successfully fetched {} users", response.getBody().size());
                return response.getBody();
            }
            throw new ServiceUnavailableException("User service returned empty response");
        } catch (Exception e) {
            log.error("Error fetching users from user-service: {}", e.getMessage());
            throw new ServiceUnavailableException("User service is currently unavailable");
        }
    }

    /**
     * Get user by ID
     */
    public UserServiceDTO getUserById(UUID userId) {
        log.info("Fetching user with ID: {} from user-service", userId);
        try {
            ResponseEntity<UserServiceDTO> response = userServiceClient.getUserById(userId);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                log.info("Successfully fetched user: {}", response.getBody().getUsername());
                return response.getBody();
            }
            throw new ServiceUnavailableException("User not found or service unavailable");
        } catch (Exception e) {
            log.error("Error fetching user {} from user-service: {}", userId, e.getMessage());
            throw new ServiceUnavailableException("User service is currently unavailable");
        }
    }

    /**
     * Update user status (enable/disable)
     */
    public UserServiceDTO updateUserStatus(UUID userId, Boolean isActive) {
        log.info("Updating status for user {} to: {}", userId, isActive);
        try {
            ResponseEntity<UserServiceDTO> response = userServiceClient.updateUserStatus(userId, isActive);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                log.info("Successfully updated user status");
                return response.getBody();
            }
            throw new ServiceUnavailableException("Failed to update user status");
        } catch (Exception e) {
            log.error("Error updating user status: {}", e.getMessage());
            throw new ServiceUnavailableException("User service is currently unavailable");
        }
    }

    /**
     * Update user role
     */
    public UserServiceDTO updateUserRole(UUID userId, String role) {
        log.info("Updating role for user {} to: {}", userId, role);
        try {
            ResponseEntity<UserServiceDTO> response = userServiceClient.updateUserRole(userId, role);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                log.info("Successfully updated user role");
                return response.getBody();
            }
            throw new ServiceUnavailableException("Failed to update user role");
        } catch (Exception e) {
            log.error("Error updating user role: {}", e.getMessage());
            throw new ServiceUnavailableException("User service is currently unavailable");
        }
    }

    /**
     * Search users by query
     */
    public List<UserServiceDTO> searchUsers(String query) {
        log.info("Searching users with query: {}", query);
        try {
            ResponseEntity<List<UserServiceDTO>> response = userServiceClient.searchUsers(query);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                log.info("Found {} users matching query", response.getBody().size());
                return response.getBody();
            }
            throw new ServiceUnavailableException("Search failed");
        } catch (Exception e) {
            log.error("Error searching users: {}", e.getMessage());
            throw new ServiceUnavailableException("User service is currently unavailable");
        }
    }

    /**
     * Get user statistics
     */
    public Object getUserStatistics() {
        log.info("Fetching user statistics");
        try {
            ResponseEntity<Object> response = userServiceClient.getUserStatistics();
            if (response.getStatusCode().is2xxSuccessful()) {
                log.info("Successfully fetched user statistics");
                return response.getBody();
            }
            throw new ServiceUnavailableException("Failed to fetch statistics");
        } catch (Exception e) {
            log.error("Error fetching user statistics: {}", e.getMessage());
            throw new ServiceUnavailableException("User service is currently unavailable");
        }
    }
}

