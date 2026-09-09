package com.tricktracker.authservice.utils;

import com.tricktracker.authservice.exception.jwt.InvalidTokenException;
import com.tricktracker.authservice.exception.jwt.TokenExpiredException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Component
@Slf4j
public class JWTProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration:3600000}")
    private long expirationTime;

    @Value("${jwt.issuer:tricktracker-auth}")
    private String issuer;

    @Value("${jwt.audience:tricktracker-client}")
    private String audience;

    private SecretKey signingKey;

    /**
     * Инициализация ключа при старте приложения
     */
    @PostConstruct
    public void init() {
        // Валидация длины ключа (минимум 256 бит для HS256)
        if (secretKey == null || secretKey.length() < 32) {
            throw new IllegalStateException(
                    "JWT secret must be at least 32 characters for HS256 algorithm");
        }
        // Кэшируем ключ с явной кодировкой
        this.signingKey = Keys.hmacShaKeyFor(
                secretKey.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Генерация access-токена
     */
    public String generateToken(String email, Map<String, Object> customClaims) {
        return generateTokenInternal(email, customClaims, expirationTime, "access");
    }
    public long getAccessTokenExpirationSeconds() {
        return expirationTime / 1000;
    }
    /**
     * Генерация refresh-токена (дольше живёт)
     */
    public String generateRefreshToken(String email, Map<String, Object> customClaims) {
        long refreshExpiration = expirationTime * 24; // 24 часа
        return generateTokenInternal(email, customClaims, refreshExpiration, "refresh");
    }

    /**
     * Внутренний метод генерации
     */
    private String generateTokenInternal(
            String email,
            Map<String, Object> customClaims,
            long ttlMillis,
            String tokenType) {

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }

        Instant now = Instant.now();
        Instant expiry = now.plusMillis(ttlMillis);

        JwtBuilder builder = Jwts.builder()
                .id(UUID.randomUUID().toString())           // jti — уникальный ID токена
                .subject(email)                              // sub — субъект
                .issuer(issuer)                              // iss — издатель
                .audience().add(audience).and()              // aud — получатель
                .issuedAt(Date.from(now))                    // iat — время выпуска
                .expiration(Date.from(expiry))               // exp — время истечения
                .claim("type", tokenType)                    // кастомный: тип токена
                .signWith(signingKey, Jwts.SIG.HS256);       // подпись

        // Добавляем кастомные claims, если есть
        if (customClaims != null && !customClaims.isEmpty()) {
            builder.claims(customClaims);
        }

        return builder.compact();
    }

    /**
     * Валидация токена + возврат Claims для дальнейшего использования
     */
    public Claims validateAndParseToken(String token) {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("Token cannot be null or empty");
        }

        try {
            return Jwts.parser()
                    .verifyWith(signingKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            throw new TokenExpiredException("Token expired", e);
        } catch (SecurityException | SignatureException e) {
            throw new InvalidTokenException("Invalid token signature", e);
        } catch (MalformedJwtException e) {
            throw new InvalidTokenException("Malformed token", e);
        } catch (UnsupportedJwtException e) {
            throw new InvalidTokenException("Unsupported token", e);
        } catch (IllegalArgumentException e) {
            throw new InvalidTokenException("Invalid token", e);
        }
    }

    /**
     * Быстрая проверка: валиден ли токен (без выброса исключений)
     */
    public boolean isTokenValid(String token) {
        try {
            validateAndParseToken(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Извлечение email (subject) из токена
     */
    public String extractEmail(String token) {
        return validateAndParseToken(token).getSubject();
    }
    /**
     * Извлечение ID (subject) из токена
     */
    public String extractUserId(String token) {
        return validateAndParseToken(token).get("userId", String.class);
    }

    /**
     * Проверка: является ли токен refresh-токеном
     */
    public boolean isRefreshToken(String token) {
        return "refresh".equals(
                validateAndParseToken(token).get("type", String.class));
    }

}