package com.tricktracker.authservice.dto;

import com.tricktracker.authservice.entity.CredentialsEntity;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class LoginResultResponse {
    private String errorCode;
    private boolean success;
    private String message;
    private UserInfo userInfo;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class UserInfo{
        private String userId;
        private String email;
    }

    public static LoginResultResponse success(CredentialsEntity credentialsEntity) {
        return LoginResultResponse.builder()
                .success(true)
                .message("Login successful")
                .userInfo(UserInfo.builder()
                        .userId(credentialsEntity.getId().toString())
                        .email(credentialsEntity.getEmail())
                        .build())
                .build();
    }

    public static LoginResultResponse failed(String errorCode) {
        return LoginResultResponse.builder()
                .success(false)
                .errorCode(errorCode)
                .message("Authentication failed")
                .build();
    }
}
