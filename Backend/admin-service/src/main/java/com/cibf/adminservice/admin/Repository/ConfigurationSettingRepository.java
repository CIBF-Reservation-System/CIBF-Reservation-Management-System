package com.cibf.adminservice.admin.Repository;

import com.cibf.adminservice.admin.Entity.ConfigurationSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for ConfigurationSetting entity
 */
@Repository
public interface ConfigurationSettingRepository extends JpaRepository<ConfigurationSetting, Long> {

    Optional<ConfigurationSetting> findByConfigKey(String configKey);

    List<ConfigurationSetting> findByCategory(String category);

    List<ConfigurationSetting> findByIsEditable(Boolean isEditable);

    boolean existsByConfigKey(String configKey);
}

