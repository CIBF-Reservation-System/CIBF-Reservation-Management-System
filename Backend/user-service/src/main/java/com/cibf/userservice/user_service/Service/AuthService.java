package com.cibf.userservice.user_service.Service;

import com.cibf.userservice.user_service.DTO.*;
import com.cibf.userservice.user_service.Entity.Role;
import com.cibf.userservice.user_service.Entity.UserEntity;
import com.cibf.userservice.user_service.Repository.RoleRepository;
import com.cibf.userservice.user_service.Repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private  final JWTService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository, JWTService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public RegisterResponseDTO register (RegisterRequestDTO req){
        if(isUserEnable(req.getEmail()))
            return new RegisterResponseDTO(null, "user already exits in the system !");

        var userData = this.createUser(req);
        if(userData.getUserId()==null)  return new RegisterResponseDTO(null, "system error!");

        return new RegisterResponseDTO(String.format("user registered at %s", userData.getUserId()),null);
    }


    public LoginResponseDTO login(LoginRequestDTO loginData) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginData.getEmail(), loginData.getPassword())
            );
        } catch (Exception e) {
            return LoginResponseDTO.builder()
                    .error("Invalid email or password")
                    .message("error")
                    .build();
        }

        UserEntity user = userRepository.findByEmail(loginData.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found in DB"));

        // Generate token with claims
        String token = jwtService.getJWTToken(
                user.getEmail(),
                user.getRole().getRoleName(),
                user.getBusinessName()
        );

        // Build the user response
        UserResponseDTO userDto = UserResponseDTO.builder()
                .email(user.getEmail())
                .role(user.getRole().getRoleName())
                .businessName(user.getBusinessName())
                .phone(user.getPhone())
                .build();

        // Return response DTO
        return LoginResponseDTO.builder()
                .message("Login successful")
                .user(userDto)
                .build();
    }


    // check user is in the database
    private Boolean isUserEnable(String email){
        return userRepository.findByEmail(email).isPresent();
    }

    // create user
    public UserEntity createUser(RegisterRequestDTO userData){

        // Fetch role from database
        Role role = roleRepository.findByRoleName(userData.getRoleName())
                .orElseThrow(() -> new RuntimeException("Role not found: " + userData.getRoleName()));

        //  Build user entity
        UserEntity newUser = UserEntity.builder()
                .email(userData.getEmail())
                .password(passwordEncoder.encode(userData.getPassword()))
                .businessName(userData.getBusinessName())
                .contactPerson(userData.getContactPerson())
                .phone(userData.getPhone())
                .role(role)
                .build();

        return userRepository.save(newUser);
    }

}
