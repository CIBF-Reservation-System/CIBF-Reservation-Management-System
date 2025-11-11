package com.cibf.adminservice.admin.Controller;

import com.cibf.adminservice.admin.Common.ConfigValueType;
import com.cibf.adminservice.admin.DTO.Request.UpdateConfigurationRequestDTO;
import com.cibf.adminservice.admin.DTO.Response.ApiResponse;
import com.cibf.adminservice.admin.DTO.Response.ConfigurationResponseDTO;
import com.cibf.adminservice.admin.Security.SecurityContextUtil;
import com.cibf.adminservice.admin.Service.ConfigurationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Controller for managing system configuration settings
 * Base URL: /cibf/admin-service/config
 */
@RestController
@RequestMapping("/config")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Configuration Management", description = "Endpoints for managing system configuration settings")
public class ConfigurationController {

    private final ConfigurationService configurationService;

    @GetMapping
    @Operation(summary = "Get all configurations", description = "Retrieve all configuration settings")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN')")
    public ResponseEntity<ApiResponse<List<ConfigurationResponseDTO>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success("Configurations retrieved", configurationService.getAllConfigurations()));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get configuration by ID", description = "Retrieve a configuration by its ID")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN')")
    public ResponseEntity<ApiResponse<ConfigurationResponseDTO>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Configuration retrieved", configurationService.getConfigurationById(id)));
    }

    @GetMapping("/key/{key}")
    @Operation(summary = "Get configuration by key", description = "Retrieve a configuration by its key")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN')")
    public ResponseEntity<ApiResponse<ConfigurationResponseDTO>> getByKey(@PathVariable String key) {
        return ResponseEntity.ok(ApiResponse.success("Configuration retrieved", configurationService.getConfigurationByKey(key)));
    }

    @GetMapping("/category/{category}")
    @Operation(summary = "Get configurations by category", description = "Retrieve configurations filtered by category")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN')")
    public ResponseEntity<ApiResponse<List<ConfigurationResponseDTO>>> getByCategory(@PathVariable String category) {
        return ResponseEntity.ok(ApiResponse.success("Configurations retrieved", configurationService.getConfigurationsByCategory(category)));
    }

    @GetMapping("/editable")
    @Operation(summary = "Get editable configurations", description = "Retrieve configurations that are editable")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','ADMIN')")
    public ResponseEntity<ApiResponse<List<ConfigurationResponseDTO>>> getEditable() {
        return ResponseEntity.ok(ApiResponse.success("Editable configurations retrieved", configurationService.getEditableConfigurations()));
    }

    @PostMapping
    @Operation(summary = "Create configuration", description = "Create a new configuration setting")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<ConfigurationResponseDTO>> create(
            @RequestParam @Parameter(description = "Configuration key") String key,
            @RequestParam @Parameter(description = "Configuration value") String value,
            @RequestParam @Parameter(description = "Value type") ConfigValueType valueType,
            @RequestParam(required = false) @Parameter(description = "Category") String category,
            @RequestParam(required = false) @Parameter(description = "Description") String description,
            @RequestParam(required = false) @Parameter(description = "Is editable") Boolean isEditable
    ) {
        UUID adminId = SecurityContextUtil.getCurrentAdminId();
        var created = configurationService.createConfiguration(key, value, valueType, category, description, isEditable, adminId);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("Configuration created", created));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update configuration", description = "Update configuration value or description")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<ConfigurationResponseDTO>> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateConfigurationRequestDTO request
    ) {
        UUID adminId = SecurityContextUtil.getCurrentAdminId();
        var updated = configurationService.updateConfiguration(id, request, adminId);
        return ResponseEntity.ok(ApiResponse.success("Configuration updated", updated));
    }

    @PutMapping("/key/{key}")
    @Operation(summary = "Update configuration by key", description = "Update configuration value by key")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<ConfigurationResponseDTO>> updateByKey(
            @PathVariable String key,
            @RequestParam String value
    ) {
        UUID adminId = SecurityContextUtil.getCurrentAdminId();
        var updated = configurationService.updateConfigurationByKey(key, value, adminId);
        return ResponseEntity.ok(ApiResponse.success("Configuration updated", updated));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete configuration", description = "Delete a configuration (editable only)")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        UUID adminId = SecurityContextUtil.getCurrentAdminId();
        configurationService.deleteConfiguration(id, adminId);
        return ResponseEntity.ok(ApiResponse.success("Configuration deleted", null));
    }
}

