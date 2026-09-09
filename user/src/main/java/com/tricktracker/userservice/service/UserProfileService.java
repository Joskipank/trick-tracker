package com.tricktracker.userservice.service;

import com.tricktracker.userservice.dto.request.CreateProfileRequest;
import com.tricktracker.userservice.dto.request.UpdateProfileRequest;
import com.tricktracker.userservice.dto.response.ProfileResponse;

import java.util.UUID;

public interface UserProfileService {
    ProfileResponse getMyProfile(UUID userId);

    ProfileResponse getProfileByUsername(String username);

    ProfileResponse updateProfile(UUID uuid, UpdateProfileRequest request);

    void createUserProfile(CreateProfileRequest request);
}
