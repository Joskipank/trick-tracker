package com.tricktracker.trickservice.dto.response;

import com.tricktracker.trickservice.enums.ProgressStatus;
import com.tricktracker.trickservice.enums.SkateLevel;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class TrickCoverImageResponse {
    private String avatarUrl;
    private String name;
    private SkateLevel skateLevel;
    private boolean isFavorite;
    private ProgressStatus progressStatus;
}
