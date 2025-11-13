package com.cibf.adminservice.admin.Security;

import com.cibf.adminservice.admin.Util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * JWT Authentication filter for admin-service
 * Validates JWT tokens from user-service and populates SecurityContext
 * No separate admin authentication - uses user-service JWT with ROLE_ORGANIZER
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, 
                                     @NonNull HttpServletResponse response, 
                                     @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }
            
            String token = authHeader.substring(7);
            if (!jwtUtil.validateToken(token)) {
                log.debug("Invalid JWT token");
                filterChain.doFilter(request, response);
                return;
            }
            
            // Extract claims from user-service JWT
            String email = jwtUtil.extractUsername(token);
            String roleName = jwtUtil.extractRole(token);
            
            // Only ROLE_ORGANIZER can access admin endpoints
            if (!"ROLE_ORGANIZER".equals(roleName)) {
                log.debug("Access denied - role {} not authorized for admin endpoints", roleName);
                filterChain.doFilter(request, response);
                return;
            }

            // Set authentication with ROLE_ORGANIZER authority
            var authority = new SimpleGrantedAuthority(roleName);
            var authentication = new UsernamePasswordAuthenticationToken(
                    email,
                    null,
                    List.of(authority)
            );
            
            // Add user details to authentication
            var details = new HashMap<String, Object>();
            details.put("email", email);
            details.put("role", roleName);
            authentication.setDetails(details);
            
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.debug("Authentication successful for user: {} with role: {}", email, roleName);
            
        } catch (Exception e) {
            log.warn("JWT authentication failed: {}", e.getMessage());
        }
        filterChain.doFilter(request, response);
    }
}
