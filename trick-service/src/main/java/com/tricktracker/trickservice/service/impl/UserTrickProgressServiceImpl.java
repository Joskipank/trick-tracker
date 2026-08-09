package com.tricktracker.trickservice.service.impl;

import com.tricktracker.trickservice.dto.request.UpdateTrickNotesRequest;
import com.tricktracker.trickservice.dto.request.UpdateTrickProgressRequest;
import com.tricktracker.trickservice.dto.response.TrickProgressResponse;
import com.tricktracker.trickservice.entity.UserTrickProgressEntity;
import com.tricktracker.trickservice.repository.UserTrickProgressRepository;
import com.tricktracker.trickservice.service.UserTrickProgressService;
import jakarta.transaction.Transactional;

public class UserTrickProgressServiceImpl implements UserTrickProgressService {
    @Override
    @Transactional(readOnly = true)
    public TrickProgressResponse getMyTricks() {
        UserTrickProgressEntity myTricks = UserTrickProgressRepository.findAllByUserId(userId)
    }

    @Override
    public TrickProgressResponse updateTrick(UpdateTrickProgressRequest request) {
        return null;
    }

    @Override
    public TrickProgressResponse createJournalEntry(UpdateTrickNotesRequest request) {
        return null;
    }
}
