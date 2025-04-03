package com.extraidados.challenge.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.postgresql.shaded.com.ongres.stringprep.ProfileName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.extraidados.challenge.entity.User;

@DataJpaTest
@ProfileName("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUsername("testereposi");
        user.setPassword("teste");
        user.setRole("usuario");
        
        user = userRepository.save(user);
    }

    @Test
    void mustSaveUser() {
        assertNotNull(user.getId());
        System.out.println(user.getId());   
    }

    @Test
    void mustFindUserById() {
        Optional<User> foundUser = userRepository.findById(user.getId());
        assertTrue(foundUser.isPresent());
        assertEquals(user.getUsername(), foundUser.get().getUsername());
        System.out.println(user.getUsername() + foundUser.get().getUsername());
    }

    @Test 
    void mustFindAllUsers() {
        List<User> users = userRepository.findAll();
        int totalusers = users.size();

        userRepository.save(userMock());
        List<User> usersDepois = userRepository.findAll();
        int totalusersDepois = usersDepois.size();
        assertNotEquals(totalusers, totalusersDepois);
        System.out.println(totalusers);
        System.out.println(totalusersDepois);
        assertFalse(users.isEmpty());
    }

    private User userMock() {
        User userMock = new User();
        userMock.setUsername("testereposição");
        userMock.setPassword("teste");
        userMock.setRole("usuario");
        return userMock;
    }
}
