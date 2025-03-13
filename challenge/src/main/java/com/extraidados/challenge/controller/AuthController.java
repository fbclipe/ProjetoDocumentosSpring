package com.extraidados.challenge.controller;

import com.extraidados.challenge.response.LoginResponse;
import com.extraidados.challenge.service.AuthTokenService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthTokenService authTokenService;

    public AuthController(AuthTokenService authTokenService) {
        this.authTokenService = authTokenService;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestParam String username, @RequestParam String password) {
        return authTokenService.login(username, password);
    }

    @PostMapping("/logout")
    public LoginResponse logout(@RequestHeader("Authorization") String token) {
        return authTokenService.logout(token);
    }

    @GetMapping("/validate")
    public LoginResponse validateToken(@RequestHeader("Authorization") String token) {
        return authTokenService.validateToken(token);
    }

    @GetMapping("/secure/dados")
    public LoginResponse getSecureData(@RequestHeader("Authorization") String token) {
        return authTokenService.getSecureData(token);
    }
}
