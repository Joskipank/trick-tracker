package com.tricktracker.userservice.controller;

import com.tricktracker.userservice.dto.request.UpdateProfileRequest;
import com.tricktracker.userservice.dto.response.ProfileResponse;
import com.tricktracker.userservice.entity.UserProfileEntity;
import com.tricktracker.userservice.service.UserProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/profiles")

public class UserProfileController {
    private final UserProfileService userProfileService;

    @GetMapping("/me")
    public ResponseEntity<ProfileResponse> getMyProfile(
            @RequestAttribute("userId") UUID authenticatedUserId // Исправили на RequestAttribute для консистентности
    ) {
        ProfileResponse response = userProfileService.getMyProfile(authenticatedUserId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{username}")
    public ResponseEntity<ProfileResponse> getProfileByUsername(@PathVariable String  username) {
        return ResponseEntity.ok(userProfileService.getProfileByUsername(username));
    }

    @PatchMapping
    public ResponseEntity<ProfileResponse> updateProfile(
            @RequestAttribute("userId") UUID userId,
            @RequestBody @Valid UpdateProfileRequest request
    ) {
        return ResponseEntity.ok(userProfileService.updateProfile(userId, request));
    }
}
