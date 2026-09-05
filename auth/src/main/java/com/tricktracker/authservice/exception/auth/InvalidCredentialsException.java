package com.tricktracker.authservice.exception.auth;

import com.tricktracker.authservice.exception.ApiException;
import org.springframework.http.HttpStatus;

public class InvalidCredentialsException extends ApiException {

    public InvalidCredentialsException() {
        super(HttpStatus.UNAUTHORIZED, "Invalid email or credentials", "INVALID_CREDENTIALS");
    }

    public InvalidCredentialsException(String message) {
        super(HttpStatus.UNAUTHORIZED, message, "INVALID_CREDENTIALS");
    }
}