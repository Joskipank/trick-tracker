package com.tricktracker.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegistrationResultResponse {

    private boolean success;
    private String errorCode;
    private String message;
    private String email;
    private int attemptsRemaining;

    public static RegistrationResultResponse success(String email) {
        return RegistrationResultResponse.builder()
                .success(true)
                .message("User registered successfully")
                .email(email)
                .build();
    }

    public static RegistrationResultResponse failed(String errorCode, int attemptsRemaining) {
        return RegistrationResultResponse.builder()
                .success(false)
                .errorCode(errorCode)
                .attemptsRemaining(attemptsRemaining)
                .build();
    }
}