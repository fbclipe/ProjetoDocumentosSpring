package com.extraidados.challenge.controller;

import com.extraidados.challenge.entity.User;
import com.extraidados.challenge.exception.ApiException;
import com.extraidados.challenge.model.LoginDto;
import com.extraidados.challenge.model.RegisterDto;
import com.extraidados.challenge.response.LoginResponse;
import com.extraidados.challenge.service.AuthTokenService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private final AuthTokenService authTokenService;
    @Autowired
    public AuthController(AuthTokenService authTokenService) {
        this.authTokenService = authTokenService;
    }
//usermodel
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
        try {
            LoginResponse login = authTokenService.login(loginDto);
            return ResponseEntity.ok(login);
            //return authTokenService.login(loginDto);
        } catch (Exception e) {
            ApiException apiException = new ApiException(e.getMessage(), HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
            //return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
            //return new LoginResponse(e.getMessage()) ; 
        }
        
    }

    @PostMapping("/register")
    public  ResponseEntity<?> registerUser(@RequestBody RegisterDto user) {
        try {
            User register = authTokenService.registerUser(user);
            return ResponseEntity.ok(register);
        } catch (Exception e) {
            ApiException apiException = new ApiException(e.getMessage(), HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
        }
        //return authTokenService.registerUser(user);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token) {
        try {
            LoginResponse logout = authTokenService.logout(token);
            return ResponseEntity.ok(logout);
        } catch (Exception e) {
            ApiException apiException = new ApiException(e.getMessage(), HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
        }
        //return authTokenService.logout(token);
    }

/*  @GetMapping("/validate")
    public boolean validateToken(@RequestHeader("Authorization") String token) {
        return authTokenService.isTokenValid(token);
    }*/
}