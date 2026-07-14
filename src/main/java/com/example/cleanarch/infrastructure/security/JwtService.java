package com.example.cleanarch.infrastructure.security;

import com.example.cleanarch.application.security.TokenService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

/**
 * HS256 JWT implementation of the {@link TokenService} port (JJWT 0.12.x API).
 *
 * <p>The signing secret and TTL are injected from configuration
 * ({@code app.jwt.secret} / {@code app.jwt.ttl-seconds}) and MUST be supplied via
 * environment variables in any real deployment - see {@code .env.example}. The
 * default in {@code application.yml} is a clearly-labelled non-production
 * placeholder that fails closed if it is left unchanged in production.
 */
@Service
public class JwtService implements TokenService {

    private final SecretKey signingKey;
    private final long ttlSeconds;

    public JwtService(
            @Value("${app.jwt.secret}") String secret,
            @Value("${app.jwt.ttl-seconds:3600}") long ttlSeconds) {
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.ttlSeconds = ttlSeconds;
    }

    @Override
    public String issueToken(String subject) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(subject)
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusSeconds(ttlSeconds)))
                .signWith(signingKey)
                .compact();
    }

    @Override
    public String resolveSubject(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(signingKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
        } catch (JwtException | IllegalArgumentException ex) {
            return null;
        }
    }
}
