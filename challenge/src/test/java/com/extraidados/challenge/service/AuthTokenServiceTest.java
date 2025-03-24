package com.extraidados.challenge.service;

import com.extraidados.challenge.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class AuthTokenServiceTest {

    private AuthTokenService authTokenService;
    private String validToken;
    private Long userId = 1L;
    private User user;

    @BeforeEach
    void setUp() {
        authTokenService = new AuthTokenService();

        validToken = UUID.randomUUID() + "-" + userId + "-" + LocalDateTime.now().plusHours(5);

        user = new User();
        user.setId(userId);
        user.setAuthToken(validToken);
    }

    @Test
    void testExtractUUIDToken() {
        String extracted = authTokenService.extractUUIDToken(validToken);
        assertEquals(validToken.split("-")[0] + "-" + validToken.split("-")[1] + "-" +
                     validToken.split("-")[2] + "-" + validToken.split("-")[3] + "-" +
                     validToken.split("-")[4], extracted);
    }

    @Test
    void testExtractIdToken() {
        Long extractedId = authTokenService.extractIdToken(validToken);
        assertEquals(userId, extractedId);
    }


    @Test
    void testGenerateToken() {
        String generatedToken = authTokenService.generateToken(userId);
        assertNotNull(generatedToken);
        assertTrue(generatedToken.contains(userId.toString()));
    }
}
