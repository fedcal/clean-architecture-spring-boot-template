package com.example.cleanarch.infrastructure.resilience;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.common.circuitbreaker.configuration.CircuitBreakerConfigCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * Example Resilience4j configuration. Defines a reusable "external" circuit
 * breaker preset that any outbound adapter (an HTTP client to a downstream
 * service, for instance) can reference with
 * {@code @CircuitBreaker(name = "external")}. Kept generic on purpose - it shows
 * the wiring pattern without coupling the template to any specific dependency.
 */
@Configuration
public class ResilienceConfig {

    @Bean
    public CircuitBreakerConfigCustomizer externalServiceCircuitBreaker() {
        return CircuitBreakerConfigCustomizer.of("external", builder -> builder
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .slidingWindowSize(20)
                .failureRateThreshold(50.0f)
                .waitDurationInOpenState(Duration.ofSeconds(30))
                .permittedNumberOfCallsInHalfOpenState(5));
    }
}
