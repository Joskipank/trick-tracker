package com.tricktracker.authservice.dto.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistrationUserRequest {
    private String email;
    private String password;
}
