package com.cibf.adminservice.admin.Service;

import com.cibf.adminservice.admin.Common.ActionStatus;
import com.cibf.adminservice.admin.Common.AlertSeverity;
import com.cibf.adminservice.admin.Common.ConfigValueType;
import com.cibf.adminservice.admin.Common.LogAction;
import com.cibf.adminservice.admin.DTO.Request.UpdateConfigurationRequestDTO;
import com.cibf.adminservice.admin.DTO.Response.ConfigurationResponseDTO;
import com.cibf.adminservice.admin.Entity.ConfigurationSetting;
import com.cibf.adminservice.admin.Exception.BadRequestException;
import com.cibf.adminservice.admin.Exception.ResourceNotFoundException;
import com.cibf.adminservice.admin.Repository.ConfigurationSettingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service for managing system configuration settings
 * Provides CRUD operations for configuration key-value pairs
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class ConfigurationService {

    private final ConfigurationSettingRepository configurationSettingRepository;
    private final AuditLogService auditLogService;

    /**
     * Get all configuration settings
     */
    public List<ConfigurationResponseDTO> getAllConfigurations() {
        log.info("Fetching all configuration settings");

        List<ConfigurationSetting> settings = configurationSettingRepository.findAll();
        log.info("Found {} configuration settings", settings.size());

        return settings.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get configuration by ID
     */
    public ConfigurationResponseDTO getConfigurationById(Long configId) {
        log.info("Fetching configuration by ID: {}", configId);

        ConfigurationSetting setting = configurationSettingRepository.findById(configId)
                .orElseThrow(() -> {
                    log.warn("Configuration not found: {}", configId);
                    return new ResourceNotFoundException("Configuration setting not found");
                });

        return mapToResponseDTO(setting);
    }

    /**
     * Get configuration by key
     */
    public ConfigurationResponseDTO getConfigurationByKey(String configKey) {
        log.info("Fetching configuration by key: {}", configKey);

        ConfigurationSetting setting = configurationSettingRepository.findByConfigKey(configKey)
                .orElseThrow(() -> {
                    log.warn("Configuration not found for key: {}", configKey);
                    return new ResourceNotFoundException("Configuration setting not found");
                });

        return mapToResponseDTO(setting);
    }

    /**
     * Get configuration value by key (returns raw value)
     */
    public String getConfigurationValue(String configKey) {
        log.debug("Getting configuration value for key: {}", configKey);

        ConfigurationSetting setting = configurationSettingRepository.findByConfigKey(configKey)
                .orElseThrow(() -> new ResourceNotFoundException("Configuration setting not found"));

        return setting.getConfigValue();
    }

    /**
     * Get configurations by category
     */
    public List<ConfigurationResponseDTO> getConfigurationsByCategory(String category) {
        log.info("Fetching configurations by category: {}", category);

        List<ConfigurationSetting> settings = configurationSettingRepository.findByCategory(category);
        log.info("Found {} configuration settings in category {}", settings.size(), category);

        return settings.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Get editable configurations
     */
    public List<ConfigurationResponseDTO> getEditableConfigurations() {
        log.info("Fetching editable configuration settings");

        List<ConfigurationSetting> settings = configurationSettingRepository.findByIsEditable(true);
        log.info("Found {} editable configuration settings", settings.size());

        return settings.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Create new configuration setting
     */
    @Transactional
    public ConfigurationResponseDTO createConfiguration(
            String configKey,
            String configValue,
            ConfigValueType valueType,
            String category,
            String description,
            Boolean isEditable,
            UUID createdBy
    ) {
        log.info("Creating new configuration: {}", configKey);

        // Check if key already exists
        if (configurationSettingRepository.existsByConfigKey(configKey)) {
            log.warn("Configuration key already exists: {}", configKey);
            throw new BadRequestException("Configuration key already exists");
        }

        // Validate value type
        validateConfigValue(configValue, valueType);

        // Create configuration setting
        ConfigurationSetting setting = new ConfigurationSetting();
        setting.setConfigKey(configKey);
        setting.setConfigValue(configValue);
        setting.setValueType(valueType);
        setting.setCategory(category);
        setting.setDescription(description);
        setting.setIsEncrypted(false);
        setting.setIsEditable(isEditable != null ? isEditable : true);
        setting.setLastModifiedBy(createdBy);

        ConfigurationSetting savedSetting = configurationSettingRepository.save(setting);
        log.info("Configuration created successfully: {} = {}", configKey, configValue);

        // Log audit
        auditLogService.logAction(
                createdBy,
                LogAction.SYSTEM_CONFIG_CHANGED,
                "ConfigurationSetting",
                savedSetting.getConfigId().toString(),
                String.format("Configuration created: %s = %s", configKey, configValue),
                (String) null,
                (String) null,
                AlertSeverity.MEDIUM,
                ActionStatus.SUCCESS,
                (String) null
        );

        return mapToResponseDTO(savedSetting);
    }

    /**
     * Update configuration setting
     */
    @Transactional
    public ConfigurationResponseDTO updateConfiguration(
            Long configId,
            UpdateConfigurationRequestDTO requestDTO,
            UUID modifiedBy
    ) {
        log.info("Updating configuration: {}", configId);

        ConfigurationSetting setting = configurationSettingRepository.findById(configId)
                .orElseThrow(() -> new ResourceNotFoundException("Configuration setting not found"));

        // Check if configuration is editable
        if (!setting.getIsEditable()) {
            log.warn("Attempt to update non-editable configuration: {}", configId);
            throw new BadRequestException("This configuration setting is not editable");
        }

        String oldValue = setting.getConfigValue();

        // Validate new value if provided
        if (requestDTO.getConfigValue() != null) {
            validateConfigValue(requestDTO.getConfigValue(), setting.getValueType());
            setting.setConfigValue(requestDTO.getConfigValue());
        }

        if (requestDTO.getDescription() != null) {
            setting.setDescription(requestDTO.getDescription());
        }

        setting.setLastModifiedBy(modifiedBy);

        ConfigurationSetting updatedSetting = configurationSettingRepository.save(setting);
        log.info("Configuration updated successfully: {} = {}", setting.getConfigKey(), setting.getConfigValue());

        // Log audit
        auditLogService.logAction(
                modifiedBy,
                LogAction.SYSTEM_CONFIG_CHANGED,
                "ConfigurationSetting",
                configId.toString(),
                String.format("Configuration updated: %s", setting.getConfigKey()),
                (String) null,
                oldValue,
                AlertSeverity.MEDIUM,
                ActionStatus.SUCCESS,
                (String) null
        );

        return mapToResponseDTO(updatedSetting);
    }

    /**
     * Update configuration value by key
     */
    @Transactional
    public ConfigurationResponseDTO updateConfigurationByKey(
            String configKey,
            String newValue,
            UUID modifiedBy
    ) {
        log.info("Updating configuration by key: {}", configKey);

        ConfigurationSetting setting = configurationSettingRepository.findByConfigKey(configKey)
                .orElseThrow(() -> new ResourceNotFoundException("Configuration setting not found"));

        // Check if configuration is editable
        if (!setting.getIsEditable()) {
            throw new BadRequestException("This configuration setting is not editable");
        }

        String oldValue = setting.getConfigValue();

        // Validate new value
        validateConfigValue(newValue, setting.getValueType());
        setting.setConfigValue(newValue);
        setting.setLastModifiedBy(modifiedBy);

        ConfigurationSetting updatedSetting = configurationSettingRepository.save(setting);
        log.info("Configuration updated successfully: {} = {}", configKey, newValue);

        // Log audit
        auditLogService.logAction(
                modifiedBy,
                LogAction.SYSTEM_CONFIG_CHANGED,
                "ConfigurationSetting",
                setting.getConfigId().toString(),
                String.format("Configuration updated: %s = %s", configKey, newValue),
                (String) null,
                oldValue,
                AlertSeverity.MEDIUM,
                ActionStatus.SUCCESS,
                (String) null
        );

        return mapToResponseDTO(updatedSetting);
    }

    /**
     * Delete configuration setting
     */
    @Transactional
    public void deleteConfiguration(Long configId, UUID deletedBy) {
        log.info("Deleting configuration: {}", configId);

        ConfigurationSetting setting = configurationSettingRepository.findById(configId)
                .orElseThrow(() -> new ResourceNotFoundException("Configuration setting not found"));

        // Check if configuration is editable
        if (!setting.getIsEditable()) {
            throw new BadRequestException("This configuration setting cannot be deleted");
        }

        String configKey = setting.getConfigKey();
        configurationSettingRepository.delete(setting);

        log.info("Configuration deleted successfully: {}", configKey);

        // Log audit
        auditLogService.logAction(
                deletedBy,
                LogAction.SYSTEM_CONFIG_CHANGED,
                "ConfigurationSetting",
                configId.toString(),
                String.format("Configuration deleted: %s", configKey),
                (String) null,
                (String) null,
                AlertSeverity.HIGH,
                ActionStatus.SUCCESS,
                (String) null
        );
    }

    /**
     * Initialize default configurations (for first-time setup)
     */
    @Transactional
    public void initializeDefaultConfigurations(UUID createdBy) {
        log.info("Initializing default configurations");

        // Only initialize if no configurations exist
        if (configurationSettingRepository.count() > 0) {
            log.info("Configurations already exist. Skipping initialization.");
            return;
        }

        // Default configurations
        createDefaultConfig("app.name", "CIBF Reservation System", ConfigValueType.STRING, "Application", "Application name", false, createdBy);
        createDefaultConfig("app.version", "1.0.0", ConfigValueType.STRING, "Application", "Application version", false, createdBy);
        createDefaultConfig("max.reservations.per.user", "5", ConfigValueType.INTEGER, "Business Rules", "Maximum reservations per user", true, createdBy);
        createDefaultConfig("booking.enabled", "true", ConfigValueType.BOOLEAN, "Business Rules", "Enable/disable booking system", true, createdBy);
        createDefaultConfig("maintenance.mode", "false", ConfigValueType.BOOLEAN, "System", "System maintenance mode", true, createdBy);
        createDefaultConfig("email.notifications.enabled", "true", ConfigValueType.BOOLEAN, "Notifications", "Enable email notifications", true, createdBy);
        createDefaultConfig("sms.notifications.enabled", "false", ConfigValueType.BOOLEAN, "Notifications", "Enable SMS notifications", true, createdBy);

        log.info("Default configurations initialized successfully");
    }

    /**
     * Helper method to create default configuration
     */
    private void createDefaultConfig(
            String key,
            String value,
            ConfigValueType type,
            String category,
            String description,
            Boolean isEditable,
            UUID createdBy
    ) {
        ConfigurationSetting setting = new ConfigurationSetting();
        setting.setConfigKey(key);
        setting.setConfigValue(value);
        setting.setValueType(type);
        setting.setCategory(category);
        setting.setDescription(description);
        setting.setIsEncrypted(false);
        setting.setIsEditable(isEditable);
        setting.setLastModifiedBy(createdBy);
        configurationSettingRepository.save(setting);
    }

    /**
     * Validate configuration value based on type
     */
    private void validateConfigValue(String value, ConfigValueType valueType) {
        try {
            switch (valueType) {
                case INTEGER:
                    Integer.parseInt(value);
                    break;
                case DECIMAL:
                    Double.parseDouble(value);
                    break;
                case BOOLEAN:
                    if (!value.equalsIgnoreCase("true") && !value.equalsIgnoreCase("false")) {
                        throw new BadRequestException("Boolean value must be 'true' or 'false'");
                    }
                    break;
                case JSON:
                    // Basic JSON validation (can be enhanced)
                    if (!value.trim().startsWith("{") && !value.trim().startsWith("[")) {
                        throw new BadRequestException("Invalid JSON format");
                    }
                    break;
                case STRING:
                    // No specific validation needed
                    break;
                default:
                    throw new BadRequestException("Unknown value type");
            }
        } catch (NumberFormatException e) {
            throw new BadRequestException("Invalid value format for type: " + valueType);
        }
    }

    /**
     * Map entity to response DTO
     */
    private ConfigurationResponseDTO mapToResponseDTO(ConfigurationSetting setting) {
        ConfigurationResponseDTO dto = new ConfigurationResponseDTO();
        dto.setConfigId(setting.getConfigId());
        dto.setConfigKey(setting.getConfigKey());
        dto.setConfigValue(setting.getConfigValue());
        dto.setValueType(setting.getValueType().name());
        dto.setDescription(setting.getDescription());
        dto.setIsEditable(setting.getIsEditable());
        return dto;
    }
}

