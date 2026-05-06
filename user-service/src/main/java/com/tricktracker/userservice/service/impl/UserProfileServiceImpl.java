package com.tricktracker.userservice.service.impl;

import com.tricktracker.userservice.dto.request.UpdateProfileRequest;
import com.tricktracker.userservice.dto.response.ProfileResponse;
import com.tricktracker.userservice.entity.UserProfileEntity;
import com.tricktracker.userservice.exception.BadRequestException;
import com.tricktracker.userservice.exception.ConflictException;
import com.tricktracker.userservice.exception.ResourceNotFoundException;
import com.tricktracker.userservice.repository.UserProfileRepository;
import com.tricktracker.userservice.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.tricktracker.userservice.enums.VisibilityLevel;

@Service
@RequiredArgsConstructor
@Transactional
public class UserProfileServiceImpl implements UserProfileService {
    //  API /users/me (мой профиль) не конфликтовал с реальным юзером
    private static final String RESERVED_USERNAME = "me";

    private final UserProfileRepository userProfileRepository;

    @Override
    @Transactional(readOnly = true)
    public ProfileResponse getMyProfile(String authenticatedPhone) {
        String normalizedPhone = normalizePhone(authenticatedPhone);

        UserProfileEntity profile = userProfileRepository.findByPhone(normalizedPhone)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return toOwnProfileResponse(profile);
    }

    @Override
    @Transactional(readOnly = true)
    public ProfileResponse getProfileByUsername(String username) {
        String normalizedUsername = normalizeUsername(username);

        if (RESERVED_USERNAME.equalsIgnoreCase(normalizedUsername)) {
            throw new BadRequestException("Username is reserved");
        }

        UserProfileEntity profile = userProfileRepository.findByUsername(normalizedUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return toPublicProfileResponse(profile);
    }

    @Override
    public ProfileResponse updateProfile(String authenticatedPhone, UpdateProfileRequest request) {
        if (request == null) {
            throw new BadRequestException("Request is required");
        }

        String normalizedPhone = normalizePhone(authenticatedPhone);

        UserProfileEntity profile = userProfileRepository.findByPhone(normalizedPhone)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (hasText(request.getUsername())) {
            String newUsername = normalizeUsername(request.getUsername());

            if (RESERVED_USERNAME.equalsIgnoreCase(newUsername)) {
                throw new BadRequestException("Username is reserved");
            }

            if (!newUsername.equals(profile.getUsername())
                    && userProfileRepository.existsByUsername(newUsername)) {
                throw new ConflictException("Username is already taken");
            }

            profile.setUsername(newUsername);
        }

        if (hasText(request.getFirstName())) {
            profile.setFirstName(request.getFirstName().trim());
        }

        if (request.getLastName() != null) {
            profile.setLastName(request.getLastName().trim());
        }

        if (request.getBio() != null) {
            profile.setBio(request.getBio().trim());
        }

        if (request.getEmail() != null) {
            profile.setEmail(request.getEmail().trim());
        }

        if (request.getPhoneVisibility() != null){
            profile.setPhoneVisibility(request.getPhoneVisibility());
        }

        if (request.getPhoneVisibility() != null){
            profile.setPhoneVisibility(request.getPhoneVisibility());
        }

        try {
            UserProfileEntity saved = userProfileRepository.save(profile);
            return toOwnProfileResponse(saved);
        } catch (DataIntegrityViolationException ex) {
            throw new ConflictException("Profile update conflict");
        }
    }

    private ProfileResponse toOwnProfileResponse(UserProfileEntity profile) {
        return ProfileResponse.builder()
                .username(profile.getUsername())
                .firstName(profile.getFirstName())
                .lastName(profile.getLastName())
                .bio(profile.getBio())
                .avatarSvg(profile.getAvatarSvg())
                .phone(profile.getPhone())
                .phoneVisibility(resolvePhoneVisibility(profile))
                .email(profile.getEmail())
                .build();
    }

    private ProfileResponse toPublicProfileResponse(UserProfileEntity profile) {
        VisibilityLevel phoneVisibility = resolvePhoneVisibility(profile);

        return ProfileResponse.builder()
                .username(profile.getUsername())
                .firstName(profile.getFirstName())
                .lastName(profile.getLastName())
                .bio(profile.getBio())
                .avatarSvg(profile.getAvatarSvg())
                .phone(phoneVisibility == VisibilityLevel.PUBLIC ? profile.getPhone() : null)
                .phoneVisibility(phoneVisibility)
                .email(profile.getEmail())
                .build();
    }

    private VisibilityLevel resolvePhoneVisibility(UserProfileEntity profile) {
        return profile.getPhoneVisibility() != null ? profile.getPhoneVisibility() : VisibilityLevel.HIDDEN;
    }

    private String normalizePhone(String phone) {
        if (!hasText(phone)) {
            throw new BadRequestException("Phone is required");
        }

        String normalized = phone.replaceAll("\\D", "");
        if (normalized.length() < 11 || normalized.length() > 15) {
            throw new BadRequestException("Invalid phone number");
        }

        return normalized;
    }

    private String normalizeUsername(String username) {
        if (!hasText(username)) {
            throw new BadRequestException("Username is required");
        }

        return username.trim();
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}
