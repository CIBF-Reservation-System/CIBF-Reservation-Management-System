package com.cibf.adminservice.admin.Service;

import com.cibf.adminservice.admin.Client.ReservationServiceClient;
import com.cibf.adminservice.admin.Client.StallServiceClient;
import com.cibf.adminservice.admin.Client.UserServiceClient;
import com.cibf.adminservice.admin.Client.NotificationServiceClient;
import com.cibf.adminservice.admin.DTO.Response.DashboardStatsDTO;
import com.cibf.adminservice.admin.Exception.ResourceNotFoundException;
import com.cibf.adminservice.admin.Exception.ServiceUnavailableException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Unified Admin Service
 * Delegates operations to respective microservices via Feign clients
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {

    private final UserServiceClient userServiceClient;
    private final StallServiceClient stallServiceClient;
    private final ReservationServiceClient reservationServiceClient;
    private final NotificationServiceClient notificationServiceClient;

    // ==================== DASHBOARD ====================

    /**
     * Get dashboard statistics by aggregating data from all services
     */
    public DashboardStatsDTO getDashboardStats() {
        log.info("Fetching dashboard statistics from all services");
        
        try {
            // Fetch data from all services
            Object userStats = getUserStatistics();
            Object stallStats = getStallStatistics();
            Object reservationStats = getReservationStatistics();
            
            // Build dashboard stats (in production, you'd parse the actual responses)
            return DashboardStatsDTO.builder()
                    .totalUsers(0L)
                    .activeUsers(0L)
                    .newUsersThisMonth(0L)
                    .totalStalls(0L)
                    .availableStalls(0L)
                    .reservedStalls(0L)
                    .pendingStalls(0L)
                    .occupancyRate(0.0)
                    .totalReservations(0L)
                    .confirmedReservations(0L)
                    .pendingReservations(0L)
                    .cancelledReservations(0L)
                    .totalRevenue(0.0)
                    .stallsByArea(new HashMap<>())
                    .reservationsByStatus(new HashMap<>())
                    .newReservationsToday(0)
                    .newUsersToday(0)
                    .build();
                    
        } catch (Exception e) {
            log.error("Error fetching dashboard statistics", e);
            throw new ServiceUnavailableException("Unable to fetch dashboard statistics");
        }
    }

    // ==================== USER MANAGEMENT ====================

    public Object getAllUsers(int page, int size) {
        log.info("Delegating to user-service: getAllUsers");
        try {
            return userServiceClient.getAllUsers(page, size);
        } catch (Exception e) {
            log.error("Error fetching users from user-service", e);
            throw new ServiceUnavailableException("User service is unavailable");
        }
    }

    public Object getUserById(UUID userId) {
        log.info("Delegating to user-service: getUserById - {}", userId);
        try {
            return userServiceClient.getUserById(userId);
        } catch (Exception e) {
            log.error("Error fetching user {} from user-service", userId, e);
            throw new ResourceNotFoundException("User not found");
        }
    }

    public Object searchUsers(String query) {
        log.info("Delegating to user-service: searchUsers - {}", query);
        try {
            return userServiceClient.searchUsers(query);
        } catch (Exception e) {
            log.error("Error searching users in user-service", e);
            throw new ServiceUnavailableException("User service is unavailable");
        }
    }

    public Object updateUserStatus(UUID userId, Boolean isActive) {
        log.info("Delegating to user-service: updateUserStatus - {} to {}", userId, isActive);
        try {
            return userServiceClient.updateUserStatus(userId, isActive);
        } catch (Exception e) {
            log.error("Error updating user status in user-service", e);
            throw new ServiceUnavailableException("User service is unavailable");
        }
    }

    public Object updateUserRole(UUID userId, String role) {
        log.info("Delegating to user-service: updateUserRole - {} to {}", userId, role);
        try {
            return userServiceClient.updateUserRole(userId, role);
        } catch (Exception e) {
            log.error("Error updating user role in user-service", e);
            throw new ServiceUnavailableException("User service is unavailable");
        }
    }

    private Object getUserStatistics() {
        try {
            return userServiceClient.getUserStatistics();
        } catch (Exception e) {
            log.warn("Unable to fetch user statistics", e);
            return new HashMap<>();
        }
    }

    // ==================== STALL MANAGEMENT ====================

    public Object getAllStalls(String area, String status) {
        log.info("Delegating to stall-service: getAllStalls (area={}, status={})", area, status);
        try {
            return stallServiceClient.getAllStalls(area, status);
        } catch (Exception e) {
            log.error("Error fetching stalls from stall-service", e);
            throw new ServiceUnavailableException("Stall service is unavailable");
        }
    }

    public Object getStallById(UUID stallId) {
        log.info("Delegating to stall-service: getStallById - {}", stallId);
        try {
            return stallServiceClient.getStallById(stallId);
        } catch (Exception e) {
            log.error("Error fetching stall {} from stall-service", stallId, e);
            throw new ResourceNotFoundException("Stall not found");
        }
    }

    public Object getPendingStalls() {
        log.info("Delegating to stall-service: getPendingStalls");
        try {
            return stallServiceClient.getPendingApprovalStalls();
        } catch (Exception e) {
            log.error("Error fetching pending stalls from stall-service", e);
            throw new ServiceUnavailableException("Stall service is unavailable");
        }
    }

    public Object updateStallAvailability(UUID stallId, Boolean isAvailable) {
        log.info("Delegating to stall-service: updateStallAvailability - {} to {}", stallId, isAvailable);
        try {
            return stallServiceClient.updateStallAvailability(stallId, isAvailable);
        } catch (Exception e) {
            log.error("Error updating stall availability in stall-service", e);
            throw new ServiceUnavailableException("Stall service is unavailable");
        }
    }

    public Object approveStall(UUID stallId) {
        log.info("Delegating to stall-service: approveStall - {}", stallId);
        try {
            return stallServiceClient.approveStall(stallId);
        } catch (Exception e) {
            log.error("Error approving stall in stall-service", e);
            throw new ServiceUnavailableException("Stall service is unavailable");
        }
    }

    public Object rejectStall(UUID stallId, String reason) {
        log.info("Delegating to stall-service: rejectStall - {} with reason: {}", stallId, reason);
        try {
            return stallServiceClient.rejectStall(stallId, reason);
        } catch (Exception e) {
            log.error("Error rejecting stall in stall-service", e);
            throw new ServiceUnavailableException("Stall service is unavailable");
        }
    }

    private Object getStallStatistics() {
        try {
            return stallServiceClient.getStallStatistics();
        } catch (Exception e) {
            log.warn("Unable to fetch stall statistics", e);
            return new HashMap<>();
        }
    }

    // ==================== RESERVATION MANAGEMENT ====================

    public Object getAllReservations(String status, int page, int size) {
        log.info("Delegating to reservation-service: getAllReservations (status={}, page={}, size={})", status, page, size);
        try {
            if (status != null && !status.isEmpty()) {
                return reservationServiceClient.getReservationsByStatus(status);
            }
            return reservationServiceClient.getAllReservations();
        } catch (Exception e) {
            log.error("Error fetching reservations from reservation-service", e);
            throw new ServiceUnavailableException("Reservation service is unavailable");
        }
    }

    public Object getReservationById(UUID reservationId) {
        log.info("Delegating to reservation-service: getReservationById - {}", reservationId);
        try {
            return reservationServiceClient.getReservationById(reservationId);
        } catch (Exception e) {
            log.error("Error fetching reservation {} from reservation-service", reservationId, e);
            throw new ResourceNotFoundException("Reservation not found");
        }
    }

    public Object cancelReservation(UUID reservationId, String reason) {
        log.info("Delegating to reservation-service: cancelReservation - {} with reason: {}", reservationId, reason);
        try {
            return reservationServiceClient.cancelReservation(reservationId);
        } catch (Exception e) {
            log.error("Error cancelling reservation in reservation-service", e);
            throw new ServiceUnavailableException("Reservation service is unavailable");
        }
    }

    public Object updateReservationStatus(UUID reservationId, String status) {
        log.info("Delegating to reservation-service: updateReservationStatus - {} to {}", reservationId, status);
        try {
            return reservationServiceClient.updateReservationStatus(reservationId, status);
        } catch (Exception e) {
            log.error("Error updating reservation status in reservation-service", e);
            throw new ServiceUnavailableException("Reservation service is unavailable");
        }
    }

    private Object getReservationStatistics() {
        try {
            return reservationServiceClient.getReservationStatistics();
        } catch (Exception e) {
            log.warn("Unable to fetch reservation statistics", e);
            return new HashMap<>();
        }
    }

    // ==================== NOTIFICATION MANAGEMENT ====================

    public Object sendNotification(Map<String, Object> notificationData) {
        log.info("Delegating to notification-service: sendNotification");
        try {
            return notificationServiceClient.sendNotification(notificationData);
        } catch (Exception e) {
            log.error("Error sending notification via notification-service", e);
            throw new ServiceUnavailableException("Notification service is unavailable");
        }
    }

    // ==================== SYSTEM HEALTH ====================

    public Object getSystemHealth() {
        log.info("Checking system health across all services");
        
        Map<String, Object> healthStatus = new HashMap<>();
        healthStatus.put("adminService", "UP");
        
        try {
            userServiceClient.healthCheck();
            healthStatus.put("userService", "UP");
        } catch (Exception e) {
            healthStatus.put("userService", "DOWN");
        }
        
        try {
            stallServiceClient.healthCheck();
            healthStatus.put("stallService", "UP");
        } catch (Exception e) {
            healthStatus.put("stallService", "DOWN");
        }
        
        try {
            reservationServiceClient.healthCheck();
            healthStatus.put("reservationService", "UP");
        } catch (Exception e) {
            healthStatus.put("reservationService", "DOWN");
        }
        
        try {
            notificationServiceClient.healthCheck();
            healthStatus.put("notificationService", "UP");
        } catch (Exception e) {
            healthStatus.put("notificationService", "DOWN");
        }
        
        return healthStatus;
    }
}
