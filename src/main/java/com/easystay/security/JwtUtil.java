package com.easystay.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;

@Component
public class JwtUtil {

    @Value("${JWT_SECRET}")
    private String jwtSecret;

    @Value("${JWT_EXPIRATION}")
    private long jwtExpirationMs;

    private SecretKey key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    // generate JWT Token，subject is username or email
    public String generateToken(String subject, String role, Long userId) {
        Date now = new Date(); // current date
        Date expiration = new Date(now.getTime() + jwtExpirationMs); // token expiration

        return Jwts.builder()
                .issuer("easystay-backend") // token issuer(from whom)
                .subject(subject) // token subject(user identifier)
                .audience().add("easystay-frontend").and()  // token audience (intended recipient, to whom)
                .expiration(expiration)
                .notBefore(now) // token valid not before this time
                .issuedAt(now) // token issued at time
                .id(UUID.randomUUID().toString()) // unique token identifier (JWT ID)
                .claim("role", role)  // custom claim: user role
                .claim("userId", userId)  // custom claim: user ID
                .signWith(key) // signature
                .compact(); // build token string
    }

    //驗證並解析 JWT Token，成功會回傳 Claims，否則丟出 JwtException。
    public Claims getClaims(String token) {
        try {
            JwtParser parser = Jwts.parser()
                    .verifyWith(key)
                    .build();

            Jws<Claims> jws = parser.parseSignedClaims(token); // check signature and expiration

            return jws.getPayload(); // 取得有效內容 (claims)
        } catch (JwtException e) {
            // Token 過期、簽名錯誤、格式不正確等都會拋出這類例外
            throw new RuntimeException("Invalid JWT Token", e);
        }
    }
    
}
