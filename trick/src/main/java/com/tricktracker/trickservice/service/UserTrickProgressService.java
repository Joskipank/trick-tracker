package com.tricktracker.trickservice.service;

import com.tricktracker.trickservice.dto.request.UpdateTrickNotesRequest;
import com.tricktracker.trickservice.dto.request.UpdateTrickProgressRequest;
import com.tricktracker.trickservice.dto.response.TrickCoverImageResponse;
import com.tricktracker.trickservice.dto.response.TrickDetailsResponse;
import com.tricktracker.trickservice.entity.UserTrickProgressEntity;
import com.tricktracker.trickservice.enums.TrickType;

import java.util.List;
import java.util.UUID;

public interface UserTrickProgressService {
    List<TrickCoverImageResponse> getTricksByType(
            UUID userId,
            TrickType type
    );

    TrickDetailsResponse getTrickDetails(UUID userId, UUID trickId);

    UserTrickProgressEntity addToFavorite(UUID userId, UUID trickId);

    UserTrickProgressEntity changeProgressStatus(UUID userId, UUID trickId);
}