package com.cibf.userservice.user_service.Service;

import com.cibf.userservice.user_service.DTO.UserListDTO;
import com.cibf.userservice.user_service.Entity.UserEntity;
import com.cibf.userservice.user_service.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // GET ALL USERS
    public List<UserListDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> new UserListDTO(
                        user.getUserId(),
                        user.getEmail(),
                        user.getBusinessName(),
                        user.getContactPerson(),
                        user.getPhone(),
                        user.getRole().getRoleName()
                )).toList();
    }

    // Find user by ID
    public UserEntity getUserById(java.util.UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}

