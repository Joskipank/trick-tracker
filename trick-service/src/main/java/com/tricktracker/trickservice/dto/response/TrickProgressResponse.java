package com.tricktracker.trickservice.dto.response;

import com.tricktracker.trickservice.enums.ProgressStatus;
import com.tricktracker.trickservice.enums.TrickCategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class TrickProgressResponse {
    private ProgressStatus status;
    private String notes;
    private TrickCategory category;
    private String slug;
}
