package com.example.cleanarch.presentation;

import com.example.cleanarch.application.security.TokenService;
import com.example.cleanarch.presentation.dto.LoginRequest;
import com.example.cleanarch.presentation.dto.TokenResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Minimal demo auth controller. It issues a JWT through the
 * {@link TokenService} application port.
 *
 * <p>Credential verification is intentionally left as a TODO for the template
 * user: wire in a UserDetailsService / AuthenticationManager check against your
 * own user store before issuing the token. The point of this class is to show
 * the presentation - to - application - port boundary, not to ship a user store.
 */
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final TokenService tokenService;

    public AuthController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public TokenResponse login(@Valid @RequestBody LoginRequest request) {
        // TODO: authenticate request.username()/request.password() against your
        // user store (AuthenticationManager) before issuing a token.
        String token = tokenService.issueToken(request.username());
        return TokenResponse.bearer(token);
    }
}
