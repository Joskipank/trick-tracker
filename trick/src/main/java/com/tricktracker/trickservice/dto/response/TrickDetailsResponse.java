package com.tricktracker.trickservice.dto.response;

import com.tricktracker.trickservice.enums.ProgressStatus;
import com.tricktracker.trickservice.enums.SkateLevel;
import com.tricktracker.trickservice.enums.TrickType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class TrickDetailsResponse {
    String name;
    String avatarSvg;
    String description;
    String videoUrl;
    SkateLevel skateLevel;
    TrickType trickType;
    ProgressStatus progressStatus;
    String notes;
    boolean isFavorite;
}
