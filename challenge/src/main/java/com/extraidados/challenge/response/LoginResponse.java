package com.extraidados.challenge.response;

public class LoginResponse {
    private String token;

    public LoginResponse(String isValid) {
        this.token = isValid;
    }

    public LoginResponse(boolean tokenValid) {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
