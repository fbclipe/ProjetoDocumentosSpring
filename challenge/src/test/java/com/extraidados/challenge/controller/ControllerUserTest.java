package com.extraidados.challenge.controller;

import com.extraidados.challenge.entity.User;
import com.extraidados.challenge.repository.UserRepository;

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

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class ControllerUserTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        User user1 = new User();
        user1.setUsername("joao");
        user1.setPassword("123");
        user1.setRole("admin");

        User user2 = new User();
        user2.setUsername("maria");
        user2.setPassword("456");
        user2.setRole("admin");

        userRepository.saveAll(Arrays.asList(user1, user2));
    }

    @Test
    @DisplayName("Deve retornar todos os usuários do banco H2")
    void mustReturnUsersFromDatabase() throws Exception {
        mockMvc.perform(get("/user/findall")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.user.length()").value(2))
            .andExpect(jsonPath("$.user[0].username").value("joao"))
            .andExpect(jsonPath("$.user[1].username").value("maria"));
    }
}
