package com.cibf.adminservice.admin.Exception;

/**
 * Exception thrown when a dependent service is unavailable
 */
public class ServiceUnavailableException extends RuntimeException {

    public ServiceUnavailableException(String message) {
        super(message);
    }

    public ServiceUnavailableException(String serviceName, Throwable cause) {
        super(String.format("Service '%s' is unavailable: %s", serviceName, cause.getMessage()), cause);
    }
}

