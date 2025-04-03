package com.extraidados.challenge.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.extraidados.challenge.entity.User;
import com.extraidados.challenge.exception.ApiException;
import com.extraidados.challenge.response.ApiListUsers;
import com.extraidados.challenge.service.UserService;

@RestController
@RequestMapping("/user")
public class ControllerUser {
    private final UserService userService;

    public ControllerUser(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/findall")
    public ResponseEntity<?> findAllUsers() {
        try {
            List<User> user = userService.findAllUser();
            ApiListUsers response = new ApiListUsers(user);
           return ResponseEntity.ok(response);
        } catch (Exception e) {
           ApiException apiException = new ApiException(e.getMessage(), HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(apiException, HttpStatus.BAD_REQUEST);
        }
        //return userService.findAllUser();
    }
}
