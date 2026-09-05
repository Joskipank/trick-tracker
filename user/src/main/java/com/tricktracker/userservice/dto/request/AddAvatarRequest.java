package com.tricktracker.userservice.dto.request;

import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class AddAvatarRequest {
    @Size(max = 20000)
    private String avatarSvg;
}
