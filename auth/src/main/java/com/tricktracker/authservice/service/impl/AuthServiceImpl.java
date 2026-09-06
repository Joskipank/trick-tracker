package com.tricktracker.authservice.service.impl;
import com.tricktracker.authservice.dto.LoginResultResponse;
import com.tricktracker.authservice.dto.RegistrationResultResponse;
import com.tricktracker.authservice.dto.request.RegistrationUserRequest;
import com.tricktracker.authservice.dto.request.LoginUserRequest;
import com.tricktracker.authservice.entity.CredentialsEntity;
import com.tricktracker.authservice.repository.AuthRepository;
import com.tricktracker.authservice.service.AuthService;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public RegistrationResultResponse register(RegistrationUserRequest request) {
        if (authRepository.existsByEmail(request.getEmail())) {
            return RegistrationResultResponse.builder()
                    .success(false)
                    .errorCode("USER_ALREADY_EXISTS")
                    .message("User with this phone already exists")
                    .email(request.getEmail())
                    .build();
        }

        // Создание пользователя

        CredentialsEntity credentialsUser = CredentialsEntity.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .isVerified(true)
                .isActive(true)
                .build();

        authRepository.save(credentialsUser);

        return RegistrationResultResponse.success(request.getEmail());
    }

    @Transactional
    @Override
    public LoginResultResponse login(LoginUserRequest request) {
        Optional<CredentialsEntity> credentialsEntity = authRepository.findByEmail(request.getEmail());
        if (credentialsEntity.isEmpty()) {
            return LoginResultResponse.failed("INVALID_CREDENTIALS");
        }
        CredentialsEntity credentials = credentialsEntity.get(); // Выносим в переменную для удобства

        // ИСПРАВЛЕНО: используем стандартный геттер Lombok .isVerified() вместо .getIsVerified()
        if (!credentials.isVerified()) {
            return LoginResultResponse.failed("ACCOUNT_NOT_VERIFIED");
        }

        // Проверяем, активен ли аккаунт (дополнительная базовая защита)
        if (!credentials.isActive()) {
            return LoginResultResponse.failed("ACCOUNT_BLOCKED");
        }

        if (!passwordEncoder.matches(request.getPassword(), credentials.getPassword())) {
            return LoginResultResponse.failed("INVALID_CREDENTIALS");
        }

        return LoginResultResponse.success(credentials);
    }

}
