package com.tricktracker.trickservice.dto.request;

import com.tricktracker.trickservice.enums.ProgressStatus;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class UpdateTrickNotesRequest {
    private String notes;
}
