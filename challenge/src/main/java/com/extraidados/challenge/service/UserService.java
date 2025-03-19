package com.extraidados.challenge.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.extraidados.challenge.entity.User;
import com.extraidados.challenge.model.RegisterDto;
import com.extraidados.challenge.repository.UserRepository;

@Service
public class UserService {
    
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUserAdmin(RegisterDto registerDto) {
        User user = new User();
        user.setUsername(registerDto.getUsername());
        user.setPassword(registerDto.getPassword());
        user.setRole("admin");
        return userRepository.save(user);
    }  

    public List<User> findAllUser() {
        return userRepository.findAll();

    }
}
