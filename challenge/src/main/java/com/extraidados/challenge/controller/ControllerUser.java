package com.extraidados.challenge.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.extraidados.challenge.entity.User;
import com.extraidados.challenge.service.UserService;

@RestController
@RequestMapping("/user")
public class ControllerUser {
    private final UserService userService;

    public ControllerUser(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/findall")
    public List<User> findAllUsers() {
        return userService.findAllUser();
    }
}
