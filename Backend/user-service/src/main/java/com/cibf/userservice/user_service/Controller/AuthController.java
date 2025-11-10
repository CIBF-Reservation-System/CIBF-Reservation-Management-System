package com.cibf.userservice.user_service.Controller;

import com.cibf.userservice.user_service.DTO.*;
import com.cibf.userservice.user_service.Entity.UserEntity;
import com.cibf.userservice.user_service.Repository.UserRepository;
import com.cibf.userservice.user_service.Service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/auth")
@CrossOrigin
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(@RequestBody RegisterRequestDTO req) {

        RegisterResponseDTO res = authService.register(req);
        if (res.getError() != null) return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);

        return ResponseEntity.status(HttpStatus.OK).body(res);
    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginData, HttpServletResponse response) {
        LoginResponseDTO res = authService.login(loginData);

        if (res.getError() != null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);

        // Set JWT token as HttpOnly cookie
        Cookie cookie = new Cookie("JWT_TOKEN", res.getToken());
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // Set true in production (HTTPS)
        cookie.setPath("/");
        cookie.setMaxAge(15 * 60);
        response.addCookie(cookie);

        return ResponseEntity.ok(res);
    }


    // Get current user info
//    @GetMapping("/profile")
//    public ResponseEntity<UserResponseDTO> getCurrentUser() {
//        return ResponseEntity.ok(authService.getCurrentUser());
//    }
    @GetMapping("/profile")
    public UserResponseDTO getCurrentUser() {
        return authService.getCurrentUser();
    }

}
