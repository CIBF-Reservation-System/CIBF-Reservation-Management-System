
package com.cibf.adminservice.admin.Service;

import com.cibf.adminservice.admin.Client.ReservationServiceClient;
import com.cibf.adminservice.admin.Client.StallServiceClient;
import com.cibf.adminservice.admin.Client.UserServiceClient;
import com.cibf.adminservice.admin.Client.NotificationServiceClient;
import com.cibf.adminservice.admin.DTO.Response.DashboardStatsDTO;
import com.cibf.adminservice.admin.Exception.ResourceNotFoundException;
import com.cibf.adminservice.admin.Exception.ServiceUnavailableException;
import com.cibf.adminservice.admin.DTO.Internal.NotificationServiceDTO;
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
public class AdminService {
    // ...existing code...

    // Overload for controller compatibility
    public Object getAllStalls(String area, String status) {
        // Filtering logic can be implemented if needed, for now just return all
        return getAllStalls();
    }

    public Object getStallById(UUID stallId) {
        try {
            return stallServiceClient.getStallById(stallId);
        } catch (Exception e) {
            throw new ResourceNotFoundException("Stall not found");
        }
    }

    public Object getPendingStalls() {
        try {
            return stallServiceClient.getPendingApprovalStalls();
        } catch (Exception e) {
            throw new ServiceUnavailableException("Stall service is unavailable");
        }
    }

    public Object updateReservationStatus(UUID reservationId, String status) {
        try {
            // Implement actual update logic as needed, for now just return reservation
            return reservationServiceClient.getReservationById(reservationId);
        } catch (Exception e) {
            throw new ServiceUnavailableException("Reservation service is unavailable");
        }
    }

    private final UserServiceClient userServiceClient;
    private final StallServiceClient stallServiceClient;
    private final ReservationServiceClient reservationServiceClient;
    private final NotificationServiceClient notificationServiceClient;

    public AdminService(UserServiceClient userServiceClient, StallServiceClient stallServiceClient, ReservationServiceClient reservationServiceClient, NotificationServiceClient notificationServiceClient) {
        this.userServiceClient = userServiceClient;
        this.stallServiceClient = stallServiceClient;
        this.reservationServiceClient = reservationServiceClient;
        this.notificationServiceClient = notificationServiceClient;
    }

    // ==================== DASHBOARD ====================
    public DashboardStatsDTO getDashboardStats() {
        DashboardStatsDTO dto = new DashboardStatsDTO();
        // Set default values or fetch from services as needed
        return dto;
    }

    // ...existing code...

    public Object getAllUsers(int page, int size) {
        try {
            return userServiceClient.getAllUsers(page, size);
        } catch (Exception e) {
            throw new ServiceUnavailableException("User service is unavailable");
        }
    }

    public Object getUserById(UUID userId) {
        try {
            return userServiceClient.getUserById(userId);
        } catch (Exception e) {
            throw new ServiceUnavailableException("User service is unavailable");
        }
    }

    public Object searchUsers(String query) {
        try {
            return userServiceClient.searchUsers(query);
        } catch (Exception e) {
            throw new ServiceUnavailableException("User service is unavailable");
        }
    }

    public Object updateUserStatus(UUID userId, Boolean isActive) {
        try {
            return userServiceClient.updateUserStatus(userId, isActive);
        } catch (Exception e) {
            throw new ServiceUnavailableException("User service is unavailable");
        }
    }

    public Object updateUserRole(UUID userId, String role) {
        try {
            return userServiceClient.updateUserRole(userId, role);
        } catch (Exception e) {
            throw new ServiceUnavailableException("User service is unavailable");
        }
    }



    // ==================== STALL MANAGEMENT ====================

    public Object getAllStalls() {
        try {

            return stallServiceClient.getAllStalls().getBody();

        } catch (Exception e) {
            throw new ServiceUnavailableException("Stall service is unavailable");
        }
    }

    public Object getPendingApprovalStalls() {
        try {
            return stallServiceClient.getPendingApprovalStalls();
        } catch (Exception e) {
            throw new ServiceUnavailableException("Stall service is unavailable");
        }
    }
    public Object updateStallAvailability(UUID stallId, Boolean isAvailable) {
        try {
            return stallServiceClient.updateStallAvailability(stallId, isAvailable);
        } catch (Exception e) {
            throw new ServiceUnavailableException("Stall service is unavailable");
        }
    }

    // ==================== RESERVATION MANAGEMENT ====================
    public Object getAllReservations(String status, int page, int size) {
        try {
            if (status != null && !status.isEmpty()) {
                return reservationServiceClient.getReservationsByStatus(status);
            }
            return reservationServiceClient.getAllReservations().getBody();
        } catch (Exception e) {
            throw new ServiceUnavailableException("Reservation service is unavailable");
        }
    }

    public Object getReservationById(UUID reservationId) {
        try {
            return reservationServiceClient.getReservationById(reservationId).getBody();
        } catch (Exception e) {
            throw new ResourceNotFoundException("Reservation not found");
        }
    }

    public Object cancelReservation(UUID reservationId, String reason) {
        try {
            return reservationServiceClient.cancelReservation(reservationId);
        } catch (Exception e) {
            throw new ServiceUnavailableException("Reservation service is unavailable");
        }
    }

    private Object getReservationStatistics() {
        try {
            return reservationServiceClient.getReservationStatistics();
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

    // ==================== NOTIFICATION MANAGEMENT ====================

    public Object sendNotification(Map<String, Object> notificationData) {
        try {
            NotificationServiceDTO dto = new NotificationServiceDTO();
            dto.setRecipientEmail((String) notificationData.getOrDefault("recipientEmail", null));
            dto.setRecipientPhone((String) notificationData.getOrDefault("recipientPhone", null));
            dto.setNotificationType((String) notificationData.getOrDefault("notificationType", null));
            dto.setSubject((String) notificationData.getOrDefault("subject", null));
            dto.setMessage((String) notificationData.getOrDefault("message", null));
            dto.setPriority((String) notificationData.getOrDefault("priority", null));
            return notificationServiceClient.sendNotification(dto);
        } catch (Exception e) {
            throw new ServiceUnavailableException("Notification service is unavailable");
        }
    }



    // ==================== SYSTEM HEALTH ====================
    public Object getSystemHealth() {

        Map<String, Object> healthStatus = new HashMap<>();
        healthStatus.put("adminService", "UP");

        // User service health
        try {
            userServiceClient.healthCheck();
            healthStatus.put("userService", "UP");
        } catch (Exception e) {
            healthStatus.put("userService", "DOWN");
        }

        // Stall service health (skip healthCheck)
        healthStatus.put("stallService", "UNKNOWN"); // or "UP" if you want

        // Reservation service health
        try {
            reservationServiceClient.healthCheck();
            healthStatus.put("reservationService", "UP");
        } catch (Exception e) {
            healthStatus.put("reservationService", "DOWN");
        }

        // Notification service health
        try {
            notificationServiceClient.healthCheck();
            healthStatus.put("notificationService", "UP");
        } catch (Exception e) {
            healthStatus.put("notificationService", "DOWN");
        }

        return healthStatus;
    }

}
