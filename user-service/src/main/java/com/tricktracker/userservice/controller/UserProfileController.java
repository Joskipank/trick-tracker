package com.tricktracker.userservice.controller;

import com.tricktracker.userservice.dto.request.UpdateProfileRequest;
import com.tricktracker.userservice.dto.response.ProfileResponse;
import com.tricktracker.userservice.service.UserProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/profile")

public class UserProfileController {
    private final UserProfileService userProfileService;

    @GetMapping("/me")
    public  ResponseEntity<ProfileResponse> getMyProfile(
            @RequestAttribute("authenticatedPhone") String authenticatedPhone
    ) {
        return ResponseEntity.ok(userProfileService.getMyProfile(authenticatedPhone));
    }

    @GetMapping("/{username}")
    public ResponseEntity<ProfileResponse> getProfileByUsername(@PathVariable String  username) {
        return ResponseEntity.ok(userProfileService.getProfileByUsername(username));
    }

    @PatchMapping
    public ResponseEntity<ProfileResponse> updateProfile(
            @RequestAttribute("authenticatedPhone") String authenticatedPhone,
            @RequestBody @Valid UpdateProfileRequest request
    ) {
        return ResponseEntity.ok(userProfileService.updateProfile(authenticatedPhone, request));
    }
}
