package com.tricktracker.userservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateProfileRequest {
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 20)
    @Pattern(regexp = "^[a-zA-Z0-9._]+$")
    private String username;

    private UUID userId;

    private String email;
}
