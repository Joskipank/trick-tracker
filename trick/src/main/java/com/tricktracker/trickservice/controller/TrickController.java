package com.tricktracker.trickservice.controller;

import com.tricktracker.trickservice.dto.response.TrickCoverImageResponse;
import com.tricktracker.trickservice.enums.TrickType;
import com.tricktracker.trickservice.service.UserTrickProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class TrickController {

    private final UserTrickProgressService trickService;

    @GetMapping("/tricks")
    public List<TrickCoverImageResponse> getTricksByType(
            @RequestParam TrickType type,
            Authentication authentication
    ) {


        UUID userId = UUID.fromString(authentication.getName());

        return trickService.getTricksByType(userId, type);
    }
}
