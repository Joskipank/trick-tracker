package com.tricktracker.userservice;

import com.tricktracker.userservice.dto.response.ProfileResponse;
import com.tricktracker.userservice.entity.UserProfileEntity;
import com.tricktracker.userservice.repository.UserProfileRepository;
import com.tricktracker.userservice.service.impl.UserProfileServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserProfileControllerTest {
    @Mock
    private UserProfileRepository userProfileRepository;

    @InjectMocks
    private UserProfileServiceImpl userProfileService;

    @Test
    void shouldReturnProfileWhenUserExists() {
        String username = "tony_hawk";
        UserProfileEntity entity = new UserProfileEntity();
        entity.setUsername(username);

        when(userProfileRepository.findByUsername(username)).thenReturn(Optional.of(entity));
        ProfileResponse response = userProfileService.getProfileByUsername(username);

        assertNotNull(response);
        assertEquals("tony_hawk", response.getUsername());

        // Проверяем, что сервис вообще ходил в репозиторий 1 раз
        verify(userProfileRepository, times(1)).findByUsername(username);
    }
}
