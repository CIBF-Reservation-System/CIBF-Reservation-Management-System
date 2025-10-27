package com.cibf.userservice.user_service.Service;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.util.Date;


@Service
public class JWTService {

    private final SecretKey secretKey;

    // Generate Secrete Key for use jwt
    public JWTService(@Value("${jwt.secret}") String secret) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
    }
//    public JWTService() {
//        try {
//            SecretKey k = KeyGenerator.getInstance("HmacSHA256").generateKey();
//            this.secretKey = Keys.hmacShaKeyFor(k.getEncoded());
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }

    // Generate JWT token (15 minutes validity)
    public String getJWTToken(String email, String roleName , String businessName) {
        return Jwts.builder()
                .subject(email)
                .claim("role", roleName)
                .claim("businessName", "Doe Enterprises")
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 15))
                .signWith(secretKey)
                .compact();
    }

    // Extract email (username) from token
    public String extractUsername(String token) {
        return getClaims(token).getSubject();

    }

    // Validate token: checks if username matches and token is not expired
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    // Check if token is expired
    private boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }

    // Parse and get claims
    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
