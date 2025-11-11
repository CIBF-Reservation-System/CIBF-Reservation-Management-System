package com.cibf.adminservice.admin.Config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * ModelMapper configuration for DTO to Entity mapping
 */
@Configuration
public class ModelMapperConfig {

    /**
     * Configure ModelMapper bean with strict matching strategy
     * to prevent incorrect field mappings
     */
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        // Set strict matching strategy
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setSkipNullEnabled(true)
                .setAmbiguityIgnored(false);

        return modelMapper;
    }
}

