package com.tricktracker.userservice.service;

import com.tricktracker.userservice.dto.request.UpdateProfileRequest;
import com.tricktracker.userservice.dto.response.ProfileResponse;

public interface UserProfileService {
    ProfileResponse getMyProfile(String authenticatedPhone);

    ProfileResponse getProfileByUsername(String username);

    ProfileResponse updateProfile(String authenticatedPhone, UpdateProfileRequest request);
}
