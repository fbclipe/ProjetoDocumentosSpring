package com.extraidados.challenge.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.extraidados.challenge.entity.User;
import com.extraidados.challenge.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;
    
    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        User user = new User();
        user.setUsername("felipe");
        user.setPassword("felipe1");
        user.setRole("admin");
        userRepository.save(user);
    }// register login e logout
    
    @AfterEach
    void setUpAfter() {
        userRepository.deleteAll();
    }
    

    @Test
    @DisplayName("Deve registrar um usuario")
    void mustReturnUserRegisted () throws Exception {
        MvcResult result = mockMvc.perform(post("/auth/register")
            .contentType(MediaType.APPLICATION_JSON)
            .content("{\"username\": \"felipe1\",\"password\": \"register\"}"))
        .andExpect(status().isOk())
        .andReturn();

        String response = result.getResponse().getContentAsString();
        System.out.println("resposta: " + response);        
    }

    @Test
    @DisplayName("Deve fazer o login do usuario e retornar o token do usuario")
    void mustLoginAndReturnAuthToken () throws Exception {

        String userJson = "{\"username\": \"felipe\",\"password\": \"felipe1\"}";

        MvcResult result = mockMvc.perform(post("/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(userJson))
        .andExpect(status().isOk())
        .andReturn();
        System.out.println(userJson);
        String response = result.getResponse().getContentAsString();
        System.out.println("resposta: " + response); 
    }

    @Test
    @DisplayName("Deve fazer o logout do usuario")
    void mustLogout () throws Exception {

        String userJson = "{\"username\": \"felipe\",\"password\": \"felipe1\"}";

        MvcResult result = mockMvc.perform(post("/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(userJson))
        .andExpect(status().isOk())
        .andReturn();

        String response = result.getResponse().getContentAsString();
        System.out.println("resposta: " + response); 
        
        ObjectMapper objectMapper = new ObjectMapper();
        String token = objectMapper.readTree(response).get("token").asText();

        mockMvc.perform(post("/auth/logout")
        .header("Authorization", token)
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
    }
}
