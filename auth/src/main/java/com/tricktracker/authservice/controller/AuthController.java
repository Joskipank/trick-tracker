package com.tricktracker.authservice.controller;

import com.tricktracker.authservice.dto.LoginResultResponse;
import com.tricktracker.authservice.dto.RegistrationResultResponse;
import com.tricktracker.authservice.dto.request.*;
import com.tricktracker.authservice.dto.response.ApiResponse;
import com.tricktracker.authservice.dto.response.AuthTokensResponse;
import com.tricktracker.authservice.exception.auth.InvalidCredentialsException;
import com.tricktracker.authservice.exception.jwt.InvalidTokenException;
import com.tricktracker.authservice.exception.jwt.TokenExpiredException;
import com.tricktracker.authservice.exception.auth.UserAlreadyExistsException;
import com.tricktracker.authservice.service.AuthService;
import com.tricktracker.authservice.utils.JWTProvider;
import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("auth/v1")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final JWTProvider jwtProvider;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthTokensResponse>> register(
            @Valid @RequestBody RegistrationUserRequest request) {

        try {
            //  authService.register() возвращает RegistrationResult
            RegistrationResultResponse regResult = authService.register(request);
            // Если регистрация не удалась — возвращаем ошибку
            if (!regResult.isSuccess()) {
                return ResponseEntity.badRequest()
                        .body(ApiResponse.error(400, regResult.getErrorCode(), regResult.getMessage()));
            }

            var claims = Map.<String, Object>of(
                    "phone", request.getEmail()
            );

            String access  = jwtProvider.generateToken(regResult.getEmail(), claims);
            String refresh = jwtProvider.generateRefreshToken(regResult.getEmail(), null);

            var tokens = new AuthTokensResponse(
                    access,
                    refresh,
                    "Bearer",
                    jwtProvider.getAccessTokenExpirationSeconds()
            );

            return ResponseEntity.created(URI.create("/users/" + request.getEmail()))
                    .body(ApiResponse.success("User registered successfully", tokens));

        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error(409, "Registration failed", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error(500, "Server error during registration", "INTERNAL_ERROR"));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthTokensResponse>> login(
            @Valid @RequestBody LoginUserRequest request) {

        try {
            // authService.login() возвращает LoginResult
            LoginResultResponse loginResult = authService.login(request);

            // Если вход не удался — возвращаем ошибку
            if (!loginResult.isSuccess()) {
                HttpStatus status = switch (loginResult.getErrorCode()) {
                    case "INVALID_CREDENTIALS" -> HttpStatus.UNAUTHORIZED;
                    case "ACCOUNT_NOT_VERIFIED" -> HttpStatus.FORBIDDEN;
                    default -> HttpStatus.BAD_REQUEST;
                };
                return ResponseEntity.status(status)
                        .body(ApiResponse.error(status.value(), loginResult.getErrorCode(), loginResult.getMessage()));
            }

            // Получаем данные из LoginResult.UserInfo
            String phone = loginResult.getUserInfo().getEmail();

            var claims = Map.<String, Object>of(
                    "phone", phone,
                    "userId", loginResult.getUserInfo().getUserId()
            );

            String access  = jwtProvider.generateToken(phone, claims);
            String refresh = jwtProvider.generateRefreshToken(phone, null);

            var tokens = new AuthTokensResponse(
                    access,
                    refresh,
                    "Bearer",
                    jwtProvider.getAccessTokenExpirationSeconds()
            );

            return ResponseEntity.ok(ApiResponse.success("Login successful", tokens));

        } catch (InvalidCredentialsException | UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(401, "INVALID_CREDENTIALS", "Invalid phone or password"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(ApiResponse.error(500, "Server error during login", "INTERNAL_ERROR"));
        }
    }

    // Дополнительный эндпоинт (очень рекомендуется)
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthTokensResponse>> refresh(
            @RequestBody RefreshTokenRequest request) {

        try {
            Claims claims = jwtProvider.validateAndParseToken(request.getRefreshToken());

            if (!jwtProvider.isRefreshToken(request.getRefreshToken())) {
                throw new InvalidTokenException("Not a refresh token");
            }

            String phone = claims.getSubject();


            var newAccess = jwtProvider.generateToken(phone, claims);

            var tokens = new AuthTokensResponse(
                    newAccess,
                    null,
                    "Bearer",
                    jwtProvider.getAccessTokenExpirationSeconds()
            );

            return ResponseEntity.ok(ApiResponse.success("Token refreshed", tokens));

        } catch (TokenExpiredException e) {
            return ResponseEntity.status(401)
                    .body(ApiResponse.error(401, "Refresh token expired"));
        } catch (InvalidTokenException e) {
            return ResponseEntity.status(401)
                    .body(ApiResponse.error(401, "Invalid refresh token"));
        }
    }

}