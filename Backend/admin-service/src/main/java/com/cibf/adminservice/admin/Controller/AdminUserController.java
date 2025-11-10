package com.cibf.adminservice.admin.Controller;

import com.cibf.adminservice.admin.Common.AdminRole;
import com.cibf.adminservice.admin.DTO.Request.AdminLoginRequestDTO;
import com.cibf.adminservice.admin.DTO.Request.ChangePasswordRequestDTO;
import com.cibf.adminservice.admin.DTO.Request.CreateAdminUserRequestDTO;
import com.cibf.adminservice.admin.DTO.Request.UpdateAdminUserRequestDTO;
import com.cibf.adminservice.admin.DTO.Response.AdminLoginResponseDTO;
import com.cibf.adminservice.admin.DTO.Response.AdminUserResponseDTO;
import com.cibf.adminservice.admin.DTO.Response.ApiResponse;
import com.cibf.adminservice.admin.Service.AdminUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Controller for admin user management operations
 * Base URL: /cibf/admin-service/admins
 */
@RestController
@RequestMapping("/admins")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Admin User Management", description = "Endpoints for managing admin users")
public class AdminUserController {

    private final AdminUserService adminUserService;

    /**
     * Register a new admin user
     * POST /cibf/admin-service/admins/register
     */
    @PostMapping("/register")
    @Operation(summary = "Register new admin user", description = "Create a new admin user account (SUPER_ADMIN only)")
    public ResponseEntity<ApiResponse<AdminUserResponseDTO>> registerAdmin(
            @Valid @RequestBody CreateAdminUserRequestDTO requestDTO,
            @RequestHeader("X-Admin-Id") UUID createdBy
    ) {
        log.info("POST /admins/register - Registering new admin: {}", requestDTO.getUsername());
        AdminUserResponseDTO response = adminUserService.registerAdmin(requestDTO, createdBy);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Admin user registered successfully", response));
    }

    /**
     * Admin login
     * POST /cibf/admin-service/admins/login
     */
    @PostMapping("/login")
    @Operation(summary = "Admin login", description = "Authenticate admin user and generate JWT token")
    public ResponseEntity<ApiResponse<AdminLoginResponseDTO>> login(
            @Valid @RequestBody AdminLoginRequestDTO requestDTO,
            HttpServletRequest request
    ) {
        log.info("POST /admins/login - Admin login attempt: {}", requestDTO.getUsername());
        String ipAddress = getClientIpAddress(request);
        AdminLoginResponseDTO response = adminUserService.login(requestDTO, ipAddress);
        return ResponseEntity.ok(ApiResponse.success("Login successful", response));
    }

    /**
     * Get current admin profile
     * GET /cibf/admin-service/admins/profile
     */
    @GetMapping("/profile")
    @Operation(summary = "Get current admin profile", description = "Retrieve current logged-in admin profile")
    public ResponseEntity<ApiResponse<AdminUserResponseDTO>> getProfile(
            @RequestHeader("X-Admin-Id") UUID adminId
    ) {
        log.info("GET /admins/profile - Fetching profile for admin: {}", adminId);
        AdminUserResponseDTO response = adminUserService.getAdminById(adminId);
        return ResponseEntity.ok(ApiResponse.success("Profile retrieved successfully", response));
    }

    /**
     * Update admin profile
     * PUT /cibf/admin-service/admins/profile
     */
    @PutMapping("/profile")
    @Operation(summary = "Update admin profile", description = "Update current admin user profile")
    public ResponseEntity<ApiResponse<AdminUserResponseDTO>> updateProfile(
            @RequestHeader("X-Admin-Id") UUID adminId,
            @Valid @RequestBody UpdateAdminUserRequestDTO requestDTO
    ) {
        log.info("PUT /admins/profile - Updating profile for admin: {}", adminId);
        AdminUserResponseDTO response = adminUserService.updateAdmin(adminId, requestDTO, adminId);
        return ResponseEntity.ok(ApiResponse.success("Profile updated successfully", response));
    }

    /**
     * Change password
     * PUT /cibf/admin-service/admins/change-password
     */
    @PutMapping("/change-password")
    @Operation(summary = "Change password", description = "Change current admin user password")
    public ResponseEntity<ApiResponse<Void>> changePassword(
            @RequestHeader("X-Admin-Id") UUID adminId,
            @Valid @RequestBody ChangePasswordRequestDTO requestDTO
    ) {
        log.info("PUT /admins/change-password - Changing password for admin: {}", adminId);
        adminUserService.changePassword(adminId, requestDTO, adminId);
        return ResponseEntity.ok(ApiResponse.success("Password changed successfully", null));
    }

    /**
     * Get all admin users
     * GET /cibf/admin-service/admins
     */
    @GetMapping
    @Operation(summary = "Get all admin users", description = "Retrieve all admin users (SUPER_ADMIN only)")
    public ResponseEntity<ApiResponse<List<AdminUserResponseDTO>>> getAllAdmins() {
        log.info("GET /admins - Fetching all admin users");
        List<AdminUserResponseDTO> response = adminUserService.getAllAdmins();
        return ResponseEntity.ok(ApiResponse.success("Admin users retrieved successfully", response));
    }

    /**
     * Get admin by ID
     * GET /cibf/admin-service/admins/{id}
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get admin by ID", description = "Retrieve specific admin user by ID")
    public ResponseEntity<ApiResponse<AdminUserResponseDTO>> getAdminById(
            @PathVariable @Parameter(description = "Admin user ID") UUID id
    ) {
        log.info("GET /admins/{} - Fetching admin by ID", id);
        AdminUserResponseDTO response = adminUserService.getAdminById(id);
        return ResponseEntity.ok(ApiResponse.success("Admin user retrieved successfully", response));
    }

    /**
     * Get admins by role
     * GET /cibf/admin-service/admins/role/{role}
     */
    @GetMapping("/role/{role}")
    @Operation(summary = "Get admins by role", description = "Retrieve admin users by role")
    public ResponseEntity<ApiResponse<List<AdminUserResponseDTO>>> getAdminsByRole(
            @PathVariable @Parameter(description = "Admin role") AdminRole role
    ) {
        log.info("GET /admins/role/{} - Fetching admins by role", role);
        List<AdminUserResponseDTO> response = adminUserService.getAdminsByRole(role);
        return ResponseEntity.ok(ApiResponse.success("Admin users retrieved successfully", response));
    }

    /**
     * Get active admins
     * GET /cibf/admin-service/admins/active
     */
    @GetMapping("/active")
    @Operation(summary = "Get active admin users", description = "Retrieve all active admin users")
    public ResponseEntity<ApiResponse<List<AdminUserResponseDTO>>> getActiveAdmins() {
        log.info("GET /admins/active - Fetching active admins");
        List<AdminUserResponseDTO> response = adminUserService.getActiveAdmins();
        return ResponseEntity.ok(ApiResponse.success("Active admin users retrieved successfully", response));
    }

    /**
     * Update admin status (enable/disable)
     * PUT /cibf/admin-service/admins/{id}/status
     */
    @PutMapping("/{id}/status")
    @Operation(summary = "Update admin status", description = "Enable or disable admin user account (SUPER_ADMIN only)")
    public ResponseEntity<ApiResponse<AdminUserResponseDTO>> updateAdminStatus(
            @PathVariable @Parameter(description = "Admin user ID") UUID id,
            @RequestParam @Parameter(description = "Active status (true/false)") Boolean isActive,
            @RequestHeader("X-Admin-Id") UUID modifiedBy
    ) {
        log.info("PUT /admins/{}/status - Updating admin status to: {}", id, isActive);
        AdminUserResponseDTO response = adminUserService.updateAdminStatus(id, isActive, modifiedBy);
        return ResponseEntity.ok(ApiResponse.success(
                String.format("Admin account %s successfully", isActive ? "enabled" : "disabled"),
                response
        ));
    }

    /**
     * Update admin role
     * PUT /cibf/admin-service/admins/{id}/role
     */
    @PutMapping("/{id}/role")
    @Operation(summary = "Update admin role", description = "Update admin user role (SUPER_ADMIN only)")
    public ResponseEntity<ApiResponse<AdminUserResponseDTO>> updateAdminRole(
            @PathVariable @Parameter(description = "Admin user ID") UUID id,
            @RequestParam @Parameter(description = "New admin role") AdminRole role,
            @RequestHeader("X-Admin-Id") UUID modifiedBy
    ) {
        log.info("PUT /admins/{}/role - Updating admin role to: {}", id, role);
        AdminUserResponseDTO response = adminUserService.updateAdminRole(id, role, modifiedBy);
        return ResponseEntity.ok(ApiResponse.success("Admin role updated successfully", response));
    }

    /**
     * Delete admin user
     * DELETE /cibf/admin-service/admins/{id}
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete admin user", description = "Delete admin user account (SUPER_ADMIN only)")
    public ResponseEntity<ApiResponse<Void>> deleteAdmin(
            @PathVariable @Parameter(description = "Admin user ID") UUID id,
            @RequestHeader("X-Admin-Id") UUID deletedBy
    ) {
        log.info("DELETE /admins/{} - Deleting admin user", id);
        adminUserService.deleteAdmin(id, deletedBy);
        return ResponseEntity.ok(ApiResponse.success("Admin user deleted successfully", null));
    }

    /**
     * Get client IP address from request
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}

