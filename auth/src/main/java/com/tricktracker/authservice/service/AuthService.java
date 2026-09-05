package com.tricktracker.authservice.service;
import com.tricktracker.authservice.dto.LoginResultResponse;
import com.tricktracker.authservice.dto.RegistrationResultResponse;
import com.tricktracker.authservice.dto.request.RegistrationUserRequest;
import com.tricktracker.authservice.dto.request.LoginUserRequest;

public interface AuthService {
    RegistrationResultResponse register(RegistrationUserRequest request);
    LoginResultResponse login(LoginUserRequest request);
}
