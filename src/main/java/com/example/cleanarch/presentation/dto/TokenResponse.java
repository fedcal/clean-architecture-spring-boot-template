package com.example.cleanarch.presentation.dto;

/**
 * HTTP response body carrying an issued access token.
 */
public record TokenResponse(String accessToken, String tokenType) {

    public static TokenResponse bearer(String accessToken) {
        return new TokenResponse(accessToken, "Bearer");
    }
}
