package com.tricktracker.userservice.dto.request;
import com.tricktracker.userservice.enums.SkateLevel;
import com.tricktracker.userservice.enums.VisibilityLevel;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class UpdateProfileRequest {
    @Size(min = 2, max = 20)
    private String firstName;

    @Size(min = 2, max = 20)
    private String lastName;

    @Size(min = 3, max = 1000)
    private String bio;

    @Size(min = 3, max = 20)
    @Pattern(regexp = "^[a-zA-Z0-9._]+$")
    private String username;

    @Email
    @Size(max = 254)
    private String email;

    @Pattern(regexp = "^\\+?[1-9]\\d{6,14}$")
    private String phone;

    private VisibilityLevel phoneVisibility;

    private SkateLevel skateLevel;
}
