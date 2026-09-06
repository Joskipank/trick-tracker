package com.tricktracker.authservice;
import com.tricktracker.authservice.dto.request.RegistrationUserRequest;
import com.tricktracker.authservice.dto.request.LoginUserRequest;
import com.tricktracker.authservice.entity.CredentialsEntity;
import com.tricktracker.authservice.repository.AuthRepository;
import com.tricktracker.authservice.service.AuthService;
import com.tricktracker.authservice.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService тесты")
class AuthServiceTest {

    @Mock
    private AuthRepository authRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthServiceImpl authService;

    private RegistrationUserRequest createRegistrationRequest(String email, String password) {
        return RegistrationUserRequest.builder()
                .email(email)
                .password(password)
                .build();
    }

    private LoginUserRequest createLoginRequest(String email, String password) {
        return LoginUserRequest.builder()
                .email(email)
                .password(password)
                .build();
    }

    private CredentialsEntity createUser(UUID id, String email, String encodedPassword, boolean isVerified) {
        return CredentialsEntity.builder()
                .id(id)
                .email(email)
                .password(encodedPassword)
                .isVerified(isVerified)
                .isActive(true)
                .build();
    }

    @Nested
    @DisplayName("Регистрация пользователя")
    class RegisterTests {

        @Test
        @DisplayName("Успешная регистрация нового пользователя")
        void shouldRegisterUser_whenPhoneNotExists() {
            // Given
            var request = createRegistrationRequest("lolkeker@gmail.com", "SecurePass123");
            var encodedPassword = "$2a$10$encodedPasswordHash";

            given(authRepository.existsByEmail(request.getEmail())).willReturn(false);
            given(passwordEncoder.encode(request.getPassword())).willReturn(encodedPassword);

            // When
            var result = authService.register(request);

            // Then
            assertThat(result.isSuccess()).isTrue();
            assertThat(result.getEmail()).isEqualTo(request.getEmail());
            assertThat(result.getErrorCode()).isNull();

            // Проверяем, что пользователь сохранён с правильными данными
            ArgumentCaptor<CredentialsEntity> credentialCaptor = ArgumentCaptor.forClass(CredentialsEntity.class);
            then(authRepository).should().save(credentialCaptor.capture());

            var savedCredentials = credentialCaptor.getValue();
            assertThat(savedCredentials.getEmail()).isEqualTo(request.getEmail());
            assertThat(savedCredentials.getPassword()).isEqualTo(encodedPassword);
            assertThat(savedCredentials.isVerified()).isTrue();
            assertThat(savedCredentials.isActive()).isTrue();
        }

        @Test
        @DisplayName("Ошибка регистрации при существующем email")
        void shouldFailRegistration_whenPhoneAlreadyExists() {
            // Given
            var request = createRegistrationRequest("lolkeker@gmail.com", "AnyPass");

            given(authRepository.existsByEmail(request.getEmail())).willReturn(true);

            // When
            var result = authService.register(request);

            // Then
            assertThat(result.isSuccess()).isFalse();
            assertThat(result.getErrorCode()).isEqualTo("USER_ALREADY_EXISTS");
            assertThat(result.getMessage()).contains("already exists");
            assertThat(result.getEmail()).isEqualTo(request.getEmail());

            // Проверяем, что save НЕ вызывался
            then(authRepository).should(never()).save(any());
            then(passwordEncoder).should(never()).encode(anyString());
        }
    }

    @Nested
    @DisplayName("Авторизация пользователя")
    class LoginTests {

        @Test
        @DisplayName("Успешный вход с верными данными")
        void shouldLogin_whenCredentialsValid() {
            // Given
            var request = createLoginRequest("lolkeker@gmail.com", "SecurePass123");
            var rawPassword = request.getPassword();
            var encodedPassword = "$2a$10$encodedHash";
            var user = createUser(UUID.randomUUID(), request.getEmail(), encodedPassword, true);

            given(authRepository.findByEmail(request.getEmail())).willReturn(Optional.of(user));
            given(passwordEncoder.matches(rawPassword, encodedPassword)).willReturn(true);

            // When
            var result = authService.login(request);

            // Then
            assertThat(result.isSuccess()).isTrue();
            assertThat(result.getUserInfo().getUserId()).isEqualTo(user.getId().toString());
            assertThat(result.getErrorCode()).isNull();
        }

        @Test
        @DisplayName("Ошибка входа: пользователь не найден")
        void shouldFailLogin_whenUserNotFound() {
            // Given
            var request = createLoginRequest("lolkeker42@gmail.com", "AnyPass");

            given(authRepository.findByEmail(request.getEmail())).willReturn(Optional.empty());

            // When
            var result = authService.login(request);

            // Then
            assertThat(result.isSuccess()).isFalse();
            assertThat(result.getErrorCode()).isEqualTo("INVALID_CREDENTIALS");
            then(passwordEncoder).should(never()).matches(anyString(), anyString());
        }

        @Test
        @DisplayName("Ошибка входа: аккаунт не верифицирован")
        void shouldFailLogin_whenAccountNotVerified() {
            // Given
            var request = createLoginRequest("lolkeker@gmail.com", "AnyPass");
            var user = createUser(UUID.randomUUID(), request.getEmail(), "$2a$10$hash", false); // not verified

            given(authRepository.findByEmail(request.getEmail())).willReturn(Optional.of(user));

            // When
            var result = authService.login(request);

            // Then
            assertThat(result.isSuccess()).isFalse();
            assertThat(result.getErrorCode()).isEqualTo("ACCOUNT_NOT_VERIFIED");
            then(passwordEncoder).should(never()).matches(anyString(), anyString());
        }

        @Test
        @DisplayName("Ошибка входа: неверный пароль")
        void shouldFailLogin_whenPasswordInvalid() {
            // Given
            var request = createLoginRequest("lolkeker@gmail.com", "WrongPass");
            var encodedPassword = "$2a$10$correctHash";
            var user = createUser(UUID.randomUUID(), request.getEmail(), encodedPassword, true);

            given(authRepository.findByEmail(request.getEmail())).willReturn(Optional.of(user));
            given(passwordEncoder.matches(request.getPassword(), encodedPassword)).willReturn(false);

            // When
            var result = authService.login(request);

            // Then
            assertThat(result.isSuccess()).isFalse();
            assertThat(result.getErrorCode()).isEqualTo("INVALID_CREDENTIALS");
        }
    }
}
