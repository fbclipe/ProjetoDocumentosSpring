package com.extraidados.challenge.exception;


import org.springframework.http.HttpStatus;

public class ApiException {
    private String message;
    private int status;
    private String error;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ApiException(String message, int status, String error) {
        this.message = message;
        this.status = status;
        this.error = error;
    }

    public ApiException(String message2, HttpStatus badRequest) {
        
        HttpStatus lala = HttpStatus.resolve(badRequest.value());
        this.status = lala.value();
        this.error = lala.getReasonPhrase();
        this.message = message2;
    }
}
