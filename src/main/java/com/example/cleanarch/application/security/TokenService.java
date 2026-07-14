package com.example.cleanarch.application.security;

/**
 * Application-layer port for issuing and validating access tokens.
 *
 * <p>The presentation layer (AuthController) depends on this abstraction, not on
 * the concrete JWT implementation, which lives in
 * {@code infrastructure.security.JwtService}. Swapping the token technology
 * (e.g. opaque tokens, a different library) requires no change above this port.
 */
public interface TokenService {

    /** Issues a signed access token for the given subject (e.g. a username). */
    String issueToken(String subject);

    /** Returns the subject encoded in a valid token, or {@code null} if invalid/expired. */
    String resolveSubject(String token);
}
