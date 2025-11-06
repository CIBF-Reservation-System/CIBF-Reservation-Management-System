package com.cibf.adminservice.admin.DTO.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response DTO for configuration settings
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConfigurationResponseDTO {

    private Long configId;
    private String configKey;
    private String configValue;
    private String valueType;
    private String description;
    private Boolean isEditable;
}

