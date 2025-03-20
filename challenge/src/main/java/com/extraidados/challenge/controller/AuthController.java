package com.extraidados.challenge.controller;

import com.extraidados.challenge.entity.User;
import com.extraidados.challenge.model.LoginDto;
import com.extraidados.challenge.model.RegisterDto;
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
//usermodel
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginDto loginDto) {
        try {
            return authTokenService.login(loginDto);
        } catch (Exception e) {
            return new LoginResponse(e.getMessage()) ; 
        }
        
    }

    @PostMapping("/register")
    public  User registerUser(@RequestBody RegisterDto user) {
        return authTokenService.registerUser(user);
    }

    @PostMapping("/logout")
    public LoginResponse logout(@RequestHeader("Authorization") String token) {
        return authTokenService.logout(token);
    }

    @GetMapping("/validate")
    public boolean validateToken(@RequestHeader("Authorization") String token) {
        return authTokenService.isTokenValid(token);
    }

    @GetMapping("/secure/data")
    public LoginResponse getSecureData(@RequestHeader("Authorization") String token) {
        return authTokenService.getSecureData(token);
    }
}