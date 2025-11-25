package com.cibf.adminservice.admin.Client;

import com.cibf.adminservice.admin.DTO.Internal.UserServiceDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Fallback implementation for UserServiceClient
 * Provides default responses when user-service is unavailable
 */
//@Slf4j
//@Component
//public class UserServiceClientFallback implements UserServiceClient {
//
//    @Override
//    public ResponseEntity<List<UserServiceDTO>> getAllUsers(int page, int size) {
//        log.error("User Service is unavailable - getAllUsers fallback triggered (page={}, size={})", page, size);
//        return ResponseEntity.status(503).body(new ArrayList<>());
//    }
//
//    @Override
//    public ResponseEntity<UserServiceDTO> getUserById(UUID userId) {
//        log.error("User Service is unavailable - getUserById fallback triggered for userId: {}", userId);
//        return ResponseEntity.status(503).body(null);
//    }
//
//    @Override
//    public ResponseEntity<UserServiceDTO> getCurrentUserProfile(String token) {
//        log.error("User Service is unavailable - getCurrentUserProfile fallback triggered");
//        return ResponseEntity.status(503).body(null);
//    }
//
//    @Override
//    public ResponseEntity<UserServiceDTO> updateUserStatus(UUID userId, Boolean isActive) {
//        log.error("User Service is unavailable - updateUserStatus fallback triggered for userId: {}", userId);
//        return ResponseEntity.status(503).body(null);
//    }
//
//    @Override
//    public ResponseEntity<UserServiceDTO> updateUserRole(UUID userId, String role) {
//        log.error("User Service is unavailable - updateUserRole fallback triggered for userId: {}", userId);
//        return ResponseEntity.status(503).body(null);
//    }
//
//    @Override
//    public ResponseEntity<List<UserServiceDTO>> searchUsers(String query) {
//        log.error("User Service is unavailable - searchUsers fallback triggered");
//        return ResponseEntity.status(503).body(new ArrayList<>());
//    }
//
//    @Override
//    public ResponseEntity<Object> getUserStatistics() {
//        log.error("User Service is unavailable - getUserStatistics fallback triggered");
//        return ResponseEntity.status(503).body(null);
//    }
//
//    @Override
//    public ResponseEntity<Object> healthCheck() {
//        log.error("User Service is unavailable - healthCheck fallback triggered");
//        return ResponseEntity.status(503).body(null);
//    }
//}
@Slf4j
@Component
public class UserServiceClientFallback implements UserServiceClient {

    @Override
    public ResponseEntity<List<UserServiceDTO>> getAllUsers(int page, int size) {
        log.error("User Service is unavailable - getAllUsers fallback triggered (page={}, size={})", page, size);
        return ResponseEntity.status(503).body(new ArrayList<>());
    }

    @Override
    public ResponseEntity<UserServiceDTO> getUserById(UUID userId) {
        log.error("User Service is unavailable - getUserById fallback triggered for userId: {}", userId);
        return ResponseEntity.status(503).body(null);
    }

    @Override
    public ResponseEntity<List<UserServiceDTO>> searchUsers(String query) {
        log.error("User Service is unavailable - searchUsers fallback triggered");
        return ResponseEntity.status(503).body(new ArrayList<>());
    }

    @Override
    public ResponseEntity<UserServiceDTO> updateUserStatus(UUID userId, Boolean isActive) {
        log.error("User Service is unavailable - updateUserStatus fallback triggered for userId: {}", userId);
        return ResponseEntity.status(503).body(null);
    }

    @Override
    public ResponseEntity<UserServiceDTO> updateUserRole(UUID userId, String role) {
        log.error("User Service is unavailable - updateUserRole fallback triggered for userId: {}", userId);
        return ResponseEntity.status(503).body(null);
    }

    @Override
    public ResponseEntity<Object> healthCheck() {
        log.error("User Service is unavailable - healthCheck fallback triggered");
        return ResponseEntity.status(503).body(null);
    }
}

