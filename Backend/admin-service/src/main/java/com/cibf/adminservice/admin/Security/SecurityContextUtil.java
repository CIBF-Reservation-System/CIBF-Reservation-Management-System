package com.cibf.adminservice.admin.Security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Utility to extract current user context from Spring Security
 * Works with JWT authentication from user-service
 */
public final class SecurityContextUtil {
    private SecurityContextUtil() {}

    /**
     * Get current user email (principal)
     */
    public static String getCurrentEmail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null ? String.valueOf(auth.getPrincipal()) : null;
    }

    /**
     * Get current user role (ROLE_ORGANIZER or ROLE_PUBLISHER)
     */
    public static String getCurrentRole() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return null;
        
        // Extract full role name (e.g., "ROLE_ORGANIZER")
        return auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse(null);
    }

    /**
     * Get business name from authentication details
     */
    public static String getCurrentBusinessName() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return null;
        
        Object details = auth.getDetails();
        if (details instanceof java.util.Map<?,?> map) {
            Object businessName = map.get("businessName");
            return businessName != null ? businessName.toString() : null;
        }
        return null;
    }
    
    /**
     * Check if current user is an organizer (admin)
     */
    public static boolean isOrganizer() {
        String role = getCurrentRole();
        return "ROLE_ORGANIZER".equals(role);
    }
}

