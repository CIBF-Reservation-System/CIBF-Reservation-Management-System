package com.cibf.adminservice.admin.Controller;

import com.cibf.adminservice.admin.DTO.HealthCheckResponseDTO;
import com.cibf.adminservice.admin.Service.HealthCheckService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@CrossOrigin
@Slf4j
public class HealthCheckController {

    private final HealthCheckService healthCheckService;

    public HealthCheckController(HealthCheckService healthCheckService) {
        this.healthCheckService = healthCheckService;
    }

    @GetMapping("/health")
    public ResponseEntity<HealthCheckResponseDTO> healthCheck() {
        log.info("Health check endpoint called");
        HealthCheckResponseDTO response = healthCheckService.performHealthCheck();

        if ("HEALTHY".equals(response.getStatus())) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(503).body(response);
        }
    }

    @GetMapping("/test")
    public ResponseEntity<String> simpleTest() {
        log.info("Simple test endpoint called");
        return ResponseEntity.ok("Admin Service is running! 🚀");
    }
}

