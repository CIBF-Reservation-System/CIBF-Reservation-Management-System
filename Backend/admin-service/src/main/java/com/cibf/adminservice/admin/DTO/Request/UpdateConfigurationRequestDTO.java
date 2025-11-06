package com.cibf.adminservice.admin.DTO.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for updating configuration settings
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateConfigurationRequestDTO {

    @NotBlank(message = "Configuration value is required")
    @Size(max = 500, message = "Configuration value must not exceed 500 characters")
    private String configValue;
}

