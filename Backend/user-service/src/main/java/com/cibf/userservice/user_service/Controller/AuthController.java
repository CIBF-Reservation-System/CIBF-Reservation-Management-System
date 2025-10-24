package com.cibf.userservice.user_service.Controller;

import com.cibf.userservice.user_service.DTO.RegisterRequestDTO;
import com.cibf.userservice.user_service.DTO.RegisterResponseDTO;
import com.cibf.userservice.user_service.Entity.UserEntity;
import com.cibf.userservice.user_service.Service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
@CrossOrigin
public class AuthController {

    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(@RequestBody RegisterRequestDTO req) {

        RegisterResponseDTO res = authService.register(req);
        if (res.getError() != null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);

        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
}
