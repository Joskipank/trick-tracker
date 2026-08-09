package com.tricktracker.authservice.service;

import java.util.UUID;

public interface JwtService {
    String generateToken(UUID id);
    String validateToken(String token);
    String getClaims(String token);
}
