package com.extraidados.challenge.response;

import java.util.List;

import com.extraidados.challenge.entity.User;

public class ApiListUsers {
    private List<User> user;

    public ApiListUsers(List<User> user) {
        this.user = user;
    }

    public List<User> getUser() {
        return user;
    }

    public void setUser(List<User> user) {
        this.user = user;
    }

    
}
