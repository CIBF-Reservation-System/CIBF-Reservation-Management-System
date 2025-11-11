package com.cibf.adminservice.admin.Entity;

import com.cibf.adminservice.admin.Common.BaseEntity;
import com.cibf.adminservice.admin.Common.ConfigValueType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Entity class for configuration_settings table
 * System configuration key-value pairs
 */
@Entity
@Table(name = "configuration_settings", indexes = {
        @Index(name = "idx_config_key", columnList = "config_key"),
        @Index(name = "idx_category", columnList = "category"),
        @Index(name = "idx_updated_at", columnList = "updated_at")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ConfigurationSetting extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "config_id")
    private Long configId;

    @Column(name = "config_key", nullable = false, unique = true, length = 100)
    private String configKey;

    @Column(name = "config_value", nullable = false, columnDefinition = "TEXT")
    private String configValue;

    @Enumerated(EnumType.STRING)
    @Column(name = "value_type", nullable = false)
    private ConfigValueType valueType = ConfigValueType.STRING;

    @Column(name = "category", nullable = false, length = 50)
    private String category;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "is_encrypted", nullable = false)
    private Boolean isEncrypted = false;

    @Column(name = "is_editable", nullable = false)
    private Boolean isEditable = true;

    @Column(name = "last_modified_by", columnDefinition = "BINARY(16)")
    private UUID lastModifiedBy;
}

