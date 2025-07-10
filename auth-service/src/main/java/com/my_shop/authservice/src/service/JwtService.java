package com.my_shop.authservice.src.service;

import com.my_shop.authservice.src.config.JwtProperties;
import com.my_shop.authservice.src.dto.TokenPair;
import com.my_shop.authservice.src.model.User;
import com.my_shop.authservice.src.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;

@Service
public class JwtService {

    private final UserRepository userRepository;
    private final JwtProperties jwtProperties;

    public JwtService(UserRepository userRepository, JwtProperties jwtProperties) {
        this.userRepository = userRepository;
        this.jwtProperties = jwtProperties;
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Base64.getDecoder().decode(jwtProperties.getSecret());
        return new SecretKeySpec(keyBytes, SignatureAlgorithm.HS512.getJcaName());
    }

    private String generateRefreshToken() {
        byte[]bytes = new byte[64];
        new SecureRandom().nextBytes(bytes);
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            hexString.append(String.format("%02x", b));
        }

        return hexString.toString();
    }

    public TokenPair generateToken(String userId, String sessionId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        String accessToken = Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setId(String.valueOf(sessionId))
                .claim("role", user.getRole())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getExp()))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();

        String refreshToken = generateRefreshToken();

        return new TokenPair(accessToken, refreshToken);
    }

    public TokenPair generateExpiredToken(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Date now = new Date();
        Date expired = new Date(now.getTime() - 60_000); // Hết hạn 1 phút trước

        String accessToken = Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("role", user.getRole())
                .setIssuedAt(new Date())
                .setExpiration(expired)
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();

        String refreshToken = generateRefreshToken();

        return new TokenPair(accessToken, refreshToken);
    }

    public String extractUserId(String token) {
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return String.valueOf(claims.getSubject());
    }

    public String getRole(String token) {
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.get("role", String.class);
    }

    public Date getExpirationDate(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }

    public long getRefreshExpiration() {
        return jwtProperties.getExp();
    }
}
