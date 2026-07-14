package com.example.cleanarch.infrastructure.ratelimit;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Generic fixed-window rate-limit interceptor (per client IP). Dependency-free
 * and in-memory so the template runs with zero external infrastructure; for a
 * distributed deployment swap the counter store for Redis/Bucket4j behind the
 * same interceptor. Returns HTTP 429 when the per-window quota is exceeded.
 */
@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    private final int maxRequestsPerWindow;
    private final Duration window;
    private final Map<String, Window> counters = new ConcurrentHashMap<>();

    public RateLimitInterceptor(
            @Value("${app.ratelimit.max-requests:120}") int maxRequestsPerWindow,
            @Value("${app.ratelimit.window-seconds:60}") long windowSeconds) {
        this.maxRequestsPerWindow = maxRequestsPerWindow;
        this.window = Duration.ofSeconds(windowSeconds);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String clientKey = request.getRemoteAddr() == null ? "unknown" : request.getRemoteAddr();
        Instant now = Instant.now();
        Window w = counters.compute(clientKey, (key, existing) -> {
            if (existing == null || now.isAfter(existing.resetAt)) {
                return new Window(1, now.plus(window));
            }
            return new Window(existing.count + 1, existing.resetAt);
        });

        if (w.count > maxRequestsPerWindow) {
            response.setStatus(429); // HTTP 429 Too Many Requests
            return false;
        }
        return true;
    }

    private record Window(int count, Instant resetAt) {
    }
}
