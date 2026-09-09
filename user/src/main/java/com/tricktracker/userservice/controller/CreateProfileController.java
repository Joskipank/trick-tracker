package com.tricktracker.userservice.controller;

import com.tricktracker.userservice.dto.request.CreateProfileRequest;
import com.tricktracker.userservice.dto.request.UpdateProfileRequest;
import com.tricktracker.userservice.dto.response.ProfileResponse;
import com.tricktracker.userservice.service.UserProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/profile")
public class CreateProfileController {
    private final UserProfileService userProfileService;

    @PostMapping("/create")
    public ResponseEntity<Void> createProfile(
            @RequestBody @Valid CreateProfileRequest request) {
        userProfileService.createUserProfile(request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
