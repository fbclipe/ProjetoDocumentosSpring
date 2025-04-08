package com.extraidados.challenge.service;

import com.extraidados.challenge.entity.User;
import com.extraidados.challenge.exception.MessageException;
import com.extraidados.challenge.model.LoginDto;
import com.extraidados.challenge.repository.UserRepository;
import com.extraidados.challenge.response.LoginResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthTokenServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks 
    private AuthTokenService authTokenService;

    private String validToken;
    private String invalidToken;
    private Long userId = 1L;
    private User user;


    @BeforeEach
    @DisplayName("Dados para realizar os testes")
    void setUp() {
        validToken = authTokenService.generateToken(userId);
    
        // Criação de um token expirado com mesmo formato
        String[] parts = validToken.split("-");
        String uuid = parts[0] + "-" + parts[1] + "-" + parts[2] + "-" + parts[3] + "-" + parts[4];
        LocalDateTime expiredTime = LocalDateTime.now().minusHours(2);
        invalidToken = uuid + "-" + userId + "-" + expiredTime;
    
        user = new User();
        user.setId(userId);
        user.setAuthToken(validToken);
    }
    

    @Test
    @DisplayName("Deve extrair o token UUID")
    void mustExtractUUIDToken() {
        String extracted = authTokenService.extractUUIDToken(validToken);
        assertEquals(validToken.split("-")[0] + "-" + validToken.split("-")[1] + "-" +
                     validToken.split("-")[2] + "-" + validToken.split("-")[3] + "-" +
                     validToken.split("-")[4], extracted);
        //assertEquals(validToken.split("-")[0] + "-" + validToken.split("-")[1] + "-" +
        //validToken.split("-")[2] + "-" + validToken.split("-")[3] + "-" +
        //validToken.split("-")[4],"lalalalal");
    }

    @Test
    @DisplayName("Deve extrair o ID do token")
    void MustExtractIdToken() {
        Long extractedId = authTokenService.extractIdToken(validToken);
        assertEquals(userId, extractedId);
        //assertEquals("777",extractedId);
    }

    @Test
    @DisplayName("Deve gerar um token")
    void MustGenerateToken() {
        String generatedToken = authTokenService.generateToken(userId);
        assertNotNull(generatedToken);
        assertTrue(generatedToken.contains(userId.toString()));
        //assertFalse(generatedToken.contains(userId.toString()));
    }

    @Test
    @DisplayName("Deve detectar um token expirado")
    void MustDetectExpiredToken() {
        assertFalse(authTokenService.tokenExpired(invalidToken));
        //assertTrue(authTokenService.tokenExpired(expiredToken));
    }

    @Test
    @DisplayName("Deve detectar se o token é valido")
    void MustDetectValidToken() {
        assertTrue(authTokenService.tokenExpired(validToken));
        assertFalse(authTokenService.tokenExpired(invalidToken));
    }
    
    @Test
    @DisplayName("Deve retornar verdadeiro para token válido")
    void mustReturnTrueForValidToken() {
        // Ambiente
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Execução
        boolean result = authTokenService.isTokenValid(validToken);

        // Verificação
        assertTrue(result);
        verify(userRepository, atLeastOnce()).findById(userId);
    }

    @Test
    @DisplayName("Deve retornar falso para token expirado")
    void mustReturnFalseForExpiredToken() {

        // Execução
        boolean result = authTokenService.isTokenValid(invalidToken);

        // Verificação
        assertFalse(result);
        verify(userRepository, never()).findById(any()); // nem deveria buscar o user
    }

    @Test
    @DisplayName("Deve retornar falso se usuário não for encontrado")
    void mustReturnFalseIfUserNotFound() {
        // Ambiente
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Execução
        boolean result = authTokenService.isTokenValid(validToken);

        // Verificação
        assertFalse(result);
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    @DisplayName("Deve retornar falso se token UUID não corresponder")
    void mustReturnFalseIfTokenUUIDDoesNotMatch() {
        // Ambiente
        String anotherToken = UUID.randomUUID() + "-" + userId + "-" + LocalDateTime.now().plusHours(5);
        user.setAuthToken(anotherToken);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Execução
        boolean result = authTokenService.isTokenValid(validToken);

        // Verificação
        assertFalse(result);
        verify(userRepository, times(1)).findById(userId);
    }
    
    @Test
    @DisplayName("Deve retornar usuário ao buscar por username")
    void mustReturnUserWhenUsernameExists() {
        // Ambiente
        String username = "testuser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

         // Execução
        Optional<User> result = authTokenService.getUserbyUsername(username);

        // Verificação
        assertTrue(result.isPresent());
        assertEquals(user, result.get());
        verify(userRepository).findByUsername(username);
    }

    @Test
    @DisplayName("Deve fazer login com credenciais válidas")
    void mustLoginWithValidCredentials() {
        // Ambiente
        String username = "testuser";
        String password = "123456";
        user.setUsername(username);
        user.setPassword(password);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // Execução
        LoginDto loginDto = new LoginDto();
        loginDto.setUsername(username);
        loginDto.setPassword(password);
        LoginResponse response = authTokenService.login(loginDto);

        // Verificação
        assertNotNull(response);
        assertNotNull(response.getToken());
        verify(userRepository).save(user);
    }

    @Test
    @DisplayName("Deve lançar exceção para login com senha inválida")
    void mustThrowExceptionForInvalidPassword() {
        // Ambiente
        String username = "testuser";
        user.setUsername(username);
        user.setPassword("correctPassword");
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // Execução 
        LoginDto loginDto = new LoginDto();
        loginDto.setUsername(username);
        loginDto.setPassword("wrongPassword");
        
        // Verificação
        assertThrows(MessageException.class, () -> authTokenService.login(loginDto));
    }

    @Test
    @DisplayName("Deve fazer logout com token válido")
    void mustLogoutWithValidToken() {
        // Ambiente
        user.setAuthToken(validToken);
        user.setTokenExpiration(LocalDateTime.now().plusHours(1));
        when(userRepository.findByAuthToken(validToken)).thenReturn(Optional.of(user));

        // Execução
        LoginResponse response = authTokenService.logout(validToken);

        // Verificação
        assertNull(user.getAuthToken());
        assertNull(user.getTokenExpiration());
        verify(userRepository).save(user);
        assertNull(response.getToken());
    }

    @Test
    @DisplayName("Deve validar token corretamente")
    void mustValidateTokenCorrectly() {
        // Ambiente
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // Execução
        LoginResponse response = authTokenService.validateToken(validToken);

        // Verificação
        assertNotNull(response);
    }
}
