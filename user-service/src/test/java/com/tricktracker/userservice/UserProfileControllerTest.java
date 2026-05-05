package com.tricktracker.userservice;

import com.tricktracker.userservice.entity.UserProfileEntity;
import com.tricktracker.userservice.repository.UserProfileRepository;
import com.tricktracker.userservice.service.UserProfileService;
import com.tricktracker.userservice.service.impl.UserProfileServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    @Mock
    private UserProfileRepository userProfileRepository;

    @InjectMocks
    private UserProfileServiceImpl userProfileService;

    @Test
    void shouldReturnProfileWhenUserExists() {
        String username = "tony_hawk";
        UserProfileEntity entity = new UserProfileEntity();
        entity.setUsername(username);

        whem()
    }
}
