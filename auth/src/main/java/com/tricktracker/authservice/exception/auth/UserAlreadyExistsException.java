package com.tricktracker.authservice.exception.auth;

import com.tricktracker.authservice.exception.ApiException;
import org.springframework.http.HttpStatus;

public class UserAlreadyExistsException extends ApiException {

    public UserAlreadyExistsException(String email) {
        super(HttpStatus.CONFLICT, "User with phone " + email + " already exists", "USER_ALREADY_EXISTS");
    }
}