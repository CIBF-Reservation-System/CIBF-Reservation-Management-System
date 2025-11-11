package com.cibf.adminservice.admin.Security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

/**
 * Utility to extract current admin context from Spring Security.
 * Assumes JwtAuthenticationFilter has set Authentication with principal=username and authority ROLE_*.
 * Extend later to include additional claims if needed.
 */
public final class SecurityContextUtil {
    private SecurityContextUtil() {}

    public static UUID getCurrentAdminId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return null;
        // We stored adminId in details via JwtAuthenticationFilter enhancement (to be added)
        Object details = auth.getDetails();
        if (details instanceof java.util.Map<?,?> map) {
            Object raw = map.get("adminId");
            if (raw != null) {
                try { return UUID.fromString(raw.toString()); } catch (Exception ignored) { }
            }
        }
        return null; // Fallback until filter updated to inject adminId
    }

    public static String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null ? String.valueOf(auth.getPrincipal()) : null;
    }

    public static String getCurrentRole() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) return null;
        return auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(r -> r.startsWith("ROLE_"))
                .map(r -> r.substring(5))
                .findFirst()
                .orElse(null);
    }
}

