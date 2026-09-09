package com.tricktracker.userservice;

import com.tricktracker.userservice.dto.request.CreateProfileRequest;
import com.tricktracker.userservice.dto.request.UpdateProfileRequest;
import com.tricktracker.userservice.dto.response.ProfileResponse;
import com.tricktracker.userservice.entity.UserProfileEntity;
import com.tricktracker.userservice.enums.VisibilityLevel;
import com.tricktracker.userservice.exception.BadRequestException;
import com.tricktracker.userservice.exception.ConflictException;
import com.tricktracker.userservice.exception.ResourceNotFoundException;
import com.tricktracker.userservice.repository.UserProfileRepository;
import com.tricktracker.userservice.service.impl.UserProfileServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;
import java.util.UUID;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuthModule tests")
public class UserProfileTest {
    @Mock
    private UserProfileRepository userProfileRepository;

    @InjectMocks
    private UserProfileServiceImpl userProfileService;

    private UUID userId;
    private UserProfileEntity existingProfile;

    @BeforeEach
    void setUpUserProfile(){
        userId = UUID.randomUUID();
        existingProfile= UserProfileEntity.builder()
                .id(userId)
                .username("tony_hawk")
                .firstName("Tony")
                .lastName("Hawk")
                .bio("Skater legent")
                .email("tony@example.com")
                .phone("+41429192")
                .phoneVisibility(VisibilityLevel.HIDDEN)
                .build();

    }

    @Nested
    class GetMyProfileTests{
        @Test
        void getMyProfile_ShouldReturnProfile_WhenUserExists(){
            when(userProfileRepository.findById(userId)).thenReturn(Optional.of(existingProfile));

            ProfileResponse response = userProfileService.getMyProfile(userId);

            assertNotNull(response);
            assertEquals("tony_hawk", response.getUsername());
            assertEquals("tony@example.com", response.getEmail());
            verify(userProfileRepository, times(1)).findById(userId);
        }

        @Test
        void getMyProfile_ShouldThrowResourceNotFoundException_WhenUserDoesNotExist(){
            when(userProfileRepository.findById(userId)).thenReturn(Optional.empty());

            ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                    () -> userProfileService.getMyProfile(userId)
            );

            assertEquals("USER_NOT_FOUND", exception.getErrorCode()); // Предполагается наличие getErrorCode()
        }
    }

    @Nested
    class GetProfileByUsernameTests {
        @Test
        void getProfileByUsername_ShouldReturnPublicProfile_WhenUserExists(){
            existingProfile.setPhoneVisibility(VisibilityLevel.PUBLIC);
            String userName = "TONY_Hawk"; // for test trim and toLOwerCase
            when(userProfileRepository.findByUsername("tony_hawk")).thenReturn(Optional.of(existingProfile));

            ProfileResponse response = userProfileService.getProfileByUsername("tony_hawk");

            assertNotNull(response);
            assertEquals("tony_hawk", response.getUsername());
            assertEquals("+41429192", response.getPhone());

        }

        @Test
        void getProfileByUsername_ShouldHidePhone_WhenVisibilityIsNotPublic(){
            existingProfile.setPhoneVisibility(VisibilityLevel.HIDDEN);
            when(userProfileRepository.findByUsername("tony_hawk")).thenReturn(Optional.of(existingProfile));

            ProfileResponse response = userProfileService.getProfileByUsername("tony_hawk");

            assertNotNull(response);
            assertNull(response.getPhone());
        }

        @Test
        void getProfileByUsername_ShouldThrowBadRequestException_WhenUsernameIsMe(){
            BadRequestException exception= assertThrows(BadRequestException.class,
                    () -> userProfileService.getProfileByUsername("me"));

            assertEquals("Username is reserved", exception.getMessage());
            verify(userProfileRepository, never()).findByUsername(any());
        }

        @Test
        void  getProfileByUsername_ShouldThrowResourceNotFoundException_WhenUserDoesNotExist() {
            when(userProfileRepository.findByUsername("unknown")).thenReturn(Optional.empty());

            assertThrows(ResourceNotFoundException.class,
                    () -> userProfileService.getProfileByUsername("unknown"));
        }

        @Test
        void getProfileByUsername_ShouldThrowBadRequestException_WhenUsernameIsEmpty() {
            // Act & Assert
            assertThrows(BadRequestException.class, () -> userProfileService.getProfileByUsername("   "));
        }
    }

    @Nested
    class UpdateProfileTests {

        private UpdateProfileRequest updateRequest;

        @BeforeEach
        void setUp() {
            updateRequest = new UpdateProfileRequest();
            updateRequest.setUsername("new_username");
            updateRequest.setFirstName(" Rodney ");
            updateRequest.setLastName("Mullen");
            updateRequest.setBio("Another legend");
            updateRequest.setEmail("rodney@example.com");
            updateRequest.setPhoneVisibility(VisibilityLevel.HIDDEN);
        }

        @Test
        void updateProfile_ShouldSuccess_WhenRequestIsValid() {
            when(userProfileRepository.findById(userId)).thenReturn(Optional.of(existingProfile));
            when(userProfileRepository.existsByUsername("new_username")).thenReturn(false);
            when(userProfileRepository.save(any(UserProfileEntity.class))).
                    thenAnswer(invocation -> invocation.getArgument(0));

            ProfileResponse response = userProfileService.updateProfile(userId, updateRequest);

            assertNotNull(response);
            assertEquals("new_username", response.getUsername());
            assertEquals("Rodney", response.getFirstName()); // Проверка trim()
            assertEquals("Mullen", response.getLastName());
            verify(userProfileRepository, times(1)).save(existingProfile);
        }

        @Test
        void updateProfile_ShouldThrowBadRequestException_WhenRequestIsNull() {
            assertThrows(BadRequestException.class, () -> userProfileService.updateProfile(userId, null));
        }

        @Test
        void updateProfile_ShouldThrowBadRequestException_WhenNewUsernameIsMe() {
            updateRequest.setUsername("me");
            when(userProfileRepository.findById(userId)).thenReturn(Optional.of(existingProfile));

            assertThrows(BadRequestException.class, () -> userProfileService.updateProfile(userId, updateRequest));
        }

        @Test
        void updateProfile_ShouldThrowConflictException_WhenUsernameIsAlreadyTaken() {
            when(userProfileRepository.findById(userId)).thenReturn(Optional.of(existingProfile));
            when(userProfileRepository.existsByUsername("new_username")).thenReturn(true);

            assertThrows(ConflictException.class, () -> userProfileService.updateProfile(userId, updateRequest));
        }

        @Test
        void updateProfile_ShouldThrowConflictException_WhenDataIntegrityViolationOccurs() {
            when(userProfileRepository.findById(userId)).thenReturn(Optional.of(existingProfile));
            when(userProfileRepository.save(any(UserProfileEntity.class))).thenThrow(DataIntegrityViolationException.class);

            assertThrows(ConflictException.class, () -> userProfileService.updateProfile(userId, updateRequest));
        }
    }

    @Nested
    class CreateUserProfileTests {
        private CreateProfileRequest createRequest;

        @BeforeEach
        void setUp() {
            createRequest = new CreateProfileRequest();
            createRequest.setUsername("Bucky_Lasek");
        }

        @Test
        void createUserProfile_ShouldSuccess_WhenUsernameIsAvailable() {
            when(userProfileRepository.existsByUsername("Bucky_Lasek")).thenReturn(false);

            assertDoesNotThrow(() -> userProfileService.createUserProfile(userId, createRequest));

            verify(userProfileRepository, times(1)).save(any(UserProfileEntity.class));
        }

        @Test
        void createUserProfile_ShouldThrowConflictException_WhenUsernameAlreadyExists() {
            // Arrange
            when(userProfileRepository.existsByUsername("Bucky_Lasek")).thenReturn(true);

            // Act & Assert
            assertThrows(ConflictException.class, () -> userProfileService.createUserProfile(userId, createRequest));
            verify(userProfileRepository, never()).save(any());
        }
    }
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
