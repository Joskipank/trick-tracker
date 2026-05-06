package com.tricktracker.userservice.dto.response;

import com.tricktracker.userservice.enums.SkateLevel;
import lombok.*;
import com.tricktracker.userservice.enums.VisibilityLevel;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ProfileResponse {
    private String username;
    private String firstName;
    private String lastName;
    private String bio;
    private String avatarSvg;
    private String phone;
    private String email;
    private VisibilityLevel phoneVisibility;
    private SkateLevel skateLevel;
}
