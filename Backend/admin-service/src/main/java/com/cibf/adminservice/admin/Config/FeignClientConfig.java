package com.cibf.adminservice.admin.Config;

import feign.Logger;
import feign.RequestInterceptor;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Feign Client configuration for inter-service communication
 */
@Configuration
public class FeignClientConfig {

    /**
     * Configure Feign logging level
     */
    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    /**
     * Request interceptor to add common headers to all Feign requests
     */
    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("Content-Type", "application/json");
            requestTemplate.header("Accept", "application/json");
            requestTemplate.header("X-Service-Name", "admin-service");
        };
    }

    /**
     * Custom error decoder for handling Feign client errors
     */
    @Bean
    public ErrorDecoder errorDecoder() {
        return new CustomFeignErrorDecoder();
    }

    /**
     * Custom error decoder implementation
     */
    private static class CustomFeignErrorDecoder implements ErrorDecoder {
        private final ErrorDecoder defaultErrorDecoder = new Default();

        @Override
        public Exception decode(String methodKey, feign.Response response) {
            // Custom error handling can be added here
            // For now, use default decoder
            return defaultErrorDecoder.decode(methodKey, response);
        }
    }
}

