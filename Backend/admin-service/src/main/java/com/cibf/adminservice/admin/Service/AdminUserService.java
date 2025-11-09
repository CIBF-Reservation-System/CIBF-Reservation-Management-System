package com.cibf.adminservice.admin.Service;

import com.cibf.adminservice.admin.Common.ActionStatus;
import com.cibf.adminservice.admin.Common.AdminRole;
import com.cibf.adminservice.admin.Common.AlertSeverity;
import com.cibf.adminservice.admin.Common.LogAction;
import com.cibf.adminservice.admin.DTO.Request.AdminLoginRequestDTO;
import com.cibf.adminservice.admin.DTO.Request.ChangePasswordRequestDTO;
import com.cibf.adminservice.admin.DTO.Request.CreateAdminUserRequestDTO;
import com.cibf.adminservice.admin.DTO.Request.UpdateAdminUserRequestDTO;
import com.cibf.adminservice.admin.DTO.Response.AdminLoginResponseDTO;
import com.cibf.adminservice.admin.DTO.Response.AdminUserResponseDTO;
import com.cibf.adminservice.admin.Entity.AdminUser;
import com.cibf.adminservice.admin.Exception.BadRequestException;
import com.cibf.adminservice.admin.Exception.ResourceNotFoundException;
import com.cibf.adminservice.admin.Exception.UnauthorizedException;
import com.cibf.adminservice.admin.Repository.AdminUserRepository;
import com.cibf.adminservice.admin.Util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service for managing admin user operations
 * Handles admin user CRUD, authentication, and authorization
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AdminUserService {

    private final AdminUserRepository adminUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final ModelMapper modelMapper;
    private final AuditLogService auditLogService;

    private static final int MAX_FAILED_ATTEMPTS = 5;

    /**
     * Register a new admin user
     */
    @Transactional
    public AdminUserResponseDTO registerAdmin(CreateAdminUserRequestDTO requestDTO, UUID createdBy) {
        log.info("Registering new admin user: {}", requestDTO.getUsername());

        // Validate username uniqueness
        if (adminUserRepository.existsByUsername(requestDTO.getUsername())) {
            log.warn("Username already exists: {}", requestDTO.getUsername());
            throw new BadRequestException("Username already exists");
        }

        // Validate email uniqueness
        if (adminUserRepository.existsByEmail(requestDTO.getEmail())) {
            log.warn("Email already exists: {}", requestDTO.getEmail());
            throw new BadRequestException("Email already exists");
        }

        // Create admin user entity
        AdminUser adminUser = new AdminUser();
        adminUser.setUsername(requestDTO.getUsername());
        adminUser.setEmail(requestDTO.getEmail());
        adminUser.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        adminUser.setFullName(requestDTO.getFullName());
        adminUser.setPhone(requestDTO.getPhone());
        adminUser.setRole(requestDTO.getRole());
        adminUser.setIsActive(true);
        adminUser.setFailedLoginAttempts(0);
        adminUser.setPasswordChangedAt(LocalDateTime.now());
        adminUser.setCreatedBy(createdBy);

        AdminUser savedAdmin = adminUserRepository.save(adminUser);
        log.info("Admin user registered successfully: {} (ID: {})", savedAdmin.getUsername(), savedAdmin.getAdminId());

        // Log audit
        auditLogService.logAction(
                createdBy,
                LogAction.ADMIN_CREATED,
                "AdminUser",
                savedAdmin.getAdminId().toString(),
                "New admin user created: " + savedAdmin.getUsername(),
                null,
                null,
                AlertSeverity.MEDIUM,
                ActionStatus.SUCCESS,
                null
        );

        return mapToResponseDTO(savedAdmin);
    }

    /**
     * Authenticate admin user and generate JWT token
     */
    @Transactional
    public AdminLoginResponseDTO login(AdminLoginRequestDTO requestDTO, String ipAddress) {
        log.info("Admin login attempt: {}", requestDTO.getUsername());

        // Find admin by username
        AdminUser adminUser = adminUserRepository.findByUsername(requestDTO.getUsername())
                .orElseThrow(() -> {
                    log.warn("Admin user not found: {}", requestDTO.getUsername());
                    return new UnauthorizedException("Invalid username or password");
                });

        // Check if account is active
        if (!adminUser.getIsActive()) {
            log.warn("Inactive admin account login attempt: {}", requestDTO.getUsername());
            throw new UnauthorizedException("Account is disabled");
        }

        // Check if account is locked due to failed attempts
        if (adminUser.getFailedLoginAttempts() >= MAX_FAILED_ATTEMPTS) {
            log.warn("Account locked due to failed attempts: {}", requestDTO.getUsername());
            throw new UnauthorizedException("Account is locked. Please contact super admin.");
        }

        // Verify password
        if (!passwordEncoder.matches(requestDTO.getPassword(), adminUser.getPassword())) {
            log.warn("Invalid password for admin: {}", requestDTO.getUsername());

            // Increment failed login attempts
            adminUser.setFailedLoginAttempts(adminUser.getFailedLoginAttempts() + 1);
            adminUserRepository.save(adminUser);

            // Log audit
            auditLogService.logAction(
                    adminUser.getAdminId(),
                    LogAction.ADMIN_LOGIN,
                    "AdminUser",
                    adminUser.getAdminId().toString(),
                    "Failed login attempt",
                    ipAddress,
                    (String) null,
                    AlertSeverity.HIGH,
                    ActionStatus.FAILED,
                    "Invalid password"
            );

            throw new UnauthorizedException("Invalid username or password");
        }

        // Reset failed login attempts on successful login
        adminUser.setFailedLoginAttempts(0);
        adminUser.setLastLogin(LocalDateTime.now());
        adminUserRepository.save(adminUser);

        // Generate JWT token
        String token = jwtUtil.generateToken(
                adminUser.getAdminId(),
                adminUser.getUsername(),
                adminUser.getRole().name()
        );

        log.info("Admin login successful: {} (ID: {})", adminUser.getUsername(), adminUser.getAdminId());

        // Log audit
        auditLogService.logAction(
                adminUser.getAdminId(),
                LogAction.ADMIN_LOGIN,
                "AdminUser",
                adminUser.getAdminId().toString(),
                "Admin logged in successfully",
                ipAddress,
                (String) null,
                AlertSeverity.LOW,
                ActionStatus.SUCCESS,
                (String) null
        );

        return new AdminLoginResponseDTO(
                token,
                mapToResponseDTO(adminUser),
                "Login successful"
        );
    }

    /**
     * Get admin user by ID
     */
    public AdminUserResponseDTO getAdminById(UUID adminId) {
        log.info("Fetching admin user by ID: {}", adminId);

        AdminUser adminUser = adminUserRepository.findById(adminId)
                .orElseThrow(() -> {
                    log.warn("Admin user not found: {}", adminId);
                    return new ResourceNotFoundException("Admin user not found");
                });

        return mapToResponseDTO(adminUser);
    }

    /**
     * Get admin user by username
     */
    public AdminUserResponseDTO getAdminByUsername(String username) {
        log.info("Fetching admin user by username: {}", username);

        AdminUser adminUser = adminUserRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("Admin user not found: {}", username);
                    return new ResourceNotFoundException("Admin user not found");
                });

        return mapToResponseDTO(adminUser);
    }

    /**
     * Get all admin users
     */
    public List<AdminUserResponseDTO> getAllAdmins() {
        log.info("Fetching all admin users");

        List<AdminUser> admins = adminUserRepository.findAll();
        log.info("Found {} admin users", admins.size());

        return admins.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get admins by role
     */
    public List<AdminUserResponseDTO> getAdminsByRole(AdminRole role) {
        log.info("Fetching admin users by role: {}", role);

        List<AdminUser> admins = adminUserRepository.findByRole(role);
        log.info("Found {} admin users with role {}", admins.size(), role);

        return admins.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get active admins
     */
    public List<AdminUserResponseDTO> getActiveAdmins() {
        log.info("Fetching active admin users");

        List<AdminUser> admins = adminUserRepository.findByIsActive(true);
        log.info("Found {} active admin users", admins.size());

        return admins.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Update admin user profile
     */
    @Transactional
    public AdminUserResponseDTO updateAdmin(UUID adminId, UpdateAdminUserRequestDTO requestDTO, UUID modifiedBy) {
        log.info("Updating admin user: {}", adminId);

        AdminUser adminUser = adminUserRepository.findById(adminId)
                .orElseThrow(() -> new ResourceNotFoundException("Admin user not found"));

        String oldValues = String.format("fullName=%s, email=%s, phone=%s",
                adminUser.getFullName(), adminUser.getEmail(), adminUser.getPhone());

        // Update fields
        if (requestDTO.getFullName() != null) {
            adminUser.setFullName(requestDTO.getFullName());
        }
        if (requestDTO.getEmail() != null && !requestDTO.getEmail().equals(adminUser.getEmail())) {
            if (adminUserRepository.existsByEmail(requestDTO.getEmail())) {
                throw new BadRequestException("Email already exists");
            }
            adminUser.setEmail(requestDTO.getEmail());
        }
        if (requestDTO.getPhone() != null) {
            adminUser.setPhone(requestDTO.getPhone());
        }

        AdminUser updatedAdmin = adminUserRepository.save(adminUser);
        log.info("Admin user updated successfully: {}", adminId);

        String newValues = String.format("fullName=%s, email=%s, phone=%s",
                updatedAdmin.getFullName(), updatedAdmin.getEmail(), updatedAdmin.getPhone());

        // Log audit
        auditLogService.logAction(
                modifiedBy,
                LogAction.ADMIN_UPDATED,
                "AdminUser",
                adminId.toString(),
                "Admin user updated",
                (String) null,
                oldValues,
                AlertSeverity.LOW,
                ActionStatus.SUCCESS,
                (String) null
        );

        return mapToResponseDTO(updatedAdmin);
    }

    /**
     * Change admin password
     */
    @Transactional
    public void changePassword(UUID adminId, ChangePasswordRequestDTO requestDTO, UUID modifiedBy) {
        log.info("Changing password for admin: {}", adminId);

        AdminUser adminUser = adminUserRepository.findById(adminId)
                .orElseThrow(() -> new ResourceNotFoundException("Admin user not found"));

        // Verify old password
        if (!passwordEncoder.matches(requestDTO.getOldPassword(), adminUser.getPassword())) {
            log.warn("Invalid old password for admin: {}", adminId);
            throw new BadRequestException("Invalid old password");
        }

        // Update password
        adminUser.setPassword(passwordEncoder.encode(requestDTO.getNewPassword()));
        adminUser.setPasswordChangedAt(LocalDateTime.now());
        adminUserRepository.save(adminUser);

        log.info("Password changed successfully for admin: {}", adminId);

        // Log audit
        auditLogService.logAction(
                modifiedBy,
                LogAction.ADMIN_UPDATED,
                "AdminUser",
                adminId.toString(),
                "Admin password changed",
                (String) null,
                (String) null,
                AlertSeverity.MEDIUM,
                ActionStatus.SUCCESS,
                (String) null
        );
    }

    /**
     * Update admin role (SUPER_ADMIN only)
     */
    @Transactional
    public AdminUserResponseDTO updateAdminRole(UUID adminId, AdminRole newRole, UUID modifiedBy) {
        log.info("Updating role for admin: {} to {}", adminId, newRole);

        AdminUser adminUser = adminUserRepository.findById(adminId)
                .orElseThrow(() -> new ResourceNotFoundException("Admin user not found"));

        AdminRole oldRole = adminUser.getRole();
        adminUser.setRole(newRole);
        AdminUser updatedAdmin = adminUserRepository.save(adminUser);

        log.info("Admin role updated successfully: {} -> {}", oldRole, newRole);

        // Log audit
        auditLogService.logAction(
                modifiedBy,
                LogAction.ADMIN_UPDATED,
                "AdminUser",
                adminId.toString(),
                String.format("Admin role changed from %s to %s", oldRole, newRole),
                (String) null,
                oldRole.toString(),
                AlertSeverity.HIGH,
                ActionStatus.SUCCESS,
                (String) null
        );

        return mapToResponseDTO(updatedAdmin);
    }

    /**
     * Enable/Disable admin account
     */
    @Transactional
    public AdminUserResponseDTO updateAdminStatus(UUID adminId, Boolean isActive, UUID modifiedBy) {
        log.info("Updating status for admin: {} to {}", adminId, isActive ? "ACTIVE" : "INACTIVE");

        AdminUser adminUser = adminUserRepository.findById(adminId)
                .orElseThrow(() -> new ResourceNotFoundException("Admin user not found"));

        Boolean oldStatus = adminUser.getIsActive();
        adminUser.setIsActive(isActive);
        AdminUser updatedAdmin = adminUserRepository.save(adminUser);

        log.info("Admin status updated successfully: {} -> {}", oldStatus, isActive);

        // Log audit
        auditLogService.logAction(
                modifiedBy,
                LogAction.ADMIN_UPDATED,
                "AdminUser",
                adminId.toString(),
                String.format("Admin account %s", isActive ? "enabled" : "disabled"),
                (String) null,
                oldStatus.toString(),
                AlertSeverity.MEDIUM,
                ActionStatus.SUCCESS,
                (String) null
        );

        return mapToResponseDTO(updatedAdmin);
    }

    /**
     * Delete admin user
     */
    @Transactional
    public void deleteAdmin(UUID adminId, UUID deletedBy) {
        log.info("Deleting admin user: {}", adminId);

        AdminUser adminUser = adminUserRepository.findById(adminId)
                .orElseThrow(() -> new ResourceNotFoundException("Admin user not found"));

        String adminUsername = adminUser.getUsername();
        adminUserRepository.delete(adminUser);

        log.info("Admin user deleted successfully: {}", adminUsername);

        // Log audit
        auditLogService.logAction(
                deletedBy,
                LogAction.ADMIN_DELETED,
                "AdminUser",
                adminId.toString(),
                String.format("Admin user deleted: %s", adminUsername),
                (String) null,
                (String) null,
                AlertSeverity.HIGH,
                ActionStatus.SUCCESS,
                (String) null
        );
    }

    /**
     * Reset failed login attempts (for locked accounts)
     */
    @Transactional
    public void resetFailedAttempts(UUID adminId, UUID modifiedBy) {
        log.info("Resetting failed login attempts for admin: {}", adminId);

        AdminUser adminUser = adminUserRepository.findById(adminId)
                .orElseThrow(() -> new ResourceNotFoundException("Admin user not found"));

        adminUser.setFailedLoginAttempts(0);
        adminUserRepository.save(adminUser);

        log.info("Failed login attempts reset for admin: {}", adminId);

        // Log audit
        auditLogService.logAction(
                modifiedBy,
                LogAction.ADMIN_UPDATED,
                "AdminUser",
                adminId.toString(),
                "Failed login attempts reset",
                (String) null,
                (String) null,
                AlertSeverity.MEDIUM,
                ActionStatus.SUCCESS,
                (String) null
        );
    }

    /**
     * Map entity to response DTO
     */
    private AdminUserResponseDTO mapToResponseDTO(AdminUser adminUser) {
        return modelMapper.map(adminUser, AdminUserResponseDTO.class);
    }
}

