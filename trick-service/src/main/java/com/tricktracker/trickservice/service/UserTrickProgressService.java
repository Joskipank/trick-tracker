package com.tricktracker.trickservice.service;

import com.tricktracker.trickservice.dto.request.UpdateTrickNotesRequest;
import com.tricktracker.trickservice.dto.request.UpdateTrickProgressRequest;
import com.tricktracker.trickservice.dto.response.TrickProgressResponse;

public interface UserTrickProgressService {
    TrickProgressResponse getMyTricks();
    TrickProgressResponse updateTrick(UpdateTrickProgressRequest request);
    TrickProgressResponse createJournalEntry(UpdateTrickNotesRequest request);
}
