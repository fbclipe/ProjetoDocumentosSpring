package com.extraidados.challenge.service;

import com.extraidados.challenge.entity.User;
import com.extraidados.challenge.exception.MessageException;
import com.extraidados.challenge.repository.UserRepository;
import com.extraidados.challenge.response.LoginResponse;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthTokenService {

    private final UserRepository userRepository;
    //private final PasswordEncoder passwordEncoder;

   public AuthTokenService(UserRepository userRepository) {
        this.userRepository = userRepository;
       // this.passwordEncoder = passwordEncoder;
    }

    public String generateToken(User user) {
        String token = UUID.randomUUID().toString() + "-" + user.getId();
        user.setAuthToken(token);
        user.setTokenExpiration(LocalDateTime.now().plusSeconds(10)); 
        userRepository.save(user);
        return token;
    }

    public boolean isTokenValid(String token) {
        Optional<User> user = userRepository.findByAuthToken(token);
        return user.isPresent() && user.get().getTokenExpiration().isAfter(LocalDateTime.now());
    }

    public Optional<User> getUserByToken(String token) {
        return userRepository.findByAuthToken(token);
    }

    public LoginResponse login(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);

        if (userOpt.isEmpty()) {
            throw new MessageException("User not found.");
            //return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User or password invalid");
        }

        return new LoginResponse(userOpt.get().getAuthToken());
        //return ResponseEntity.ok(token);
    }

    public LoginResponse logout(String token) {
        Optional<User> userOpt = userRepository.findByAuthToken(token);

        if (userOpt.isEmpty()) {
            throw new MessageException("User not found.");
            //return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token expired or invalid");
        }

        User user = userOpt.get();
        user.setAuthToken(null);
        user.setTokenExpiration(null);
        userRepository.save(user);
            return new LoginResponse(userOpt.get().getAuthToken());

        //return ResponseEntity.ok("Sucessful logout");
    }

    public LoginResponse validateToken(String token) {
        boolean isValid = isTokenValid(token);
        //return ResponseEntity.ok(isValid);
                return new LoginResponse(isValid);
    }

    public LoginResponse getSecureData(String token) {
        if (!isTokenValid(token)) {
            throw new MessageException("User not found.");
            //return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token expired or invalid.");
        }
        return new LoginResponse(token);

        //return ResponseEntity.ok("Sucessful login.");
    }
}

