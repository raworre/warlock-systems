package com.warlock.user.service;

import com.warlock.user.TestUtils;
import com.warlock.user.model.ProfileUpdateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.expression.AccessException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class UserServiceUpdateProfileTest extends UserServiceTest {
    @Test
    void updateProfile_ReturnsNewProfile() throws AccessException {
        var generatedToken = TestUtils.basicToken(jwtBuilder);
        var updateRequest = ProfileUpdateRequest.builder()
                .firstName("JimBob")
                .lastName("Chucklefuck")
                .build();
        when(userRepository.findByUsername(anyString()))
                .thenReturn(TestUtils.TEST_USER);
        when(profileRepository.save(any()))
                .thenReturn(TestUtils.UPDATED_NAME_PROFILE);
        when(userRepository.save(any()))
                .thenReturn(TestUtils.UPDATED_FIRST_NAME_USER);

        var newProfile = service.updateProfile(generatedToken, updateRequest);

        verify(userRepository, times(1))
                .findByUsername(anyString());
        verify(profileRepository, times(1)).save(any());

        assertNotNull(newProfile);
        assert(newProfile.getFirstName()).equals("JimBob");
        assert(newProfile.getLastName()).equals("Chucklefuck");
    }

    @Test
    void updateProfile_ThrowsAccessException() {
        assertThrows(AccessException.class, () -> service.fetchProfile(""));
        assertThrows(AccessException.class, () -> service.fetchProfile("209alpskdhf9q82yh34akdfvnm;a"));
        assertThrows(AccessException.class, () -> service.fetchProfile("part.one.two"));
        assertThrows(AccessException.class, () -> service.fetchProfile(TestUtils.noSubToken(jwtBuilder)));

        verifyNoInteractions(userRepository);
        verifyNoInteractions(profileRepository);
    }

    @Test
    void updateProfile_ThrowsIllegalArgumentExceptionWhenRequestEmpty() {
        var token = TestUtils.basicToken(jwtBuilder);
        var updateRequest = ProfileUpdateRequest.builder().build();
        assertThrows(IllegalArgumentException.class, () -> service.updateProfile(token, updateRequest));
    }

    @Test
    void updateProfile_ThrowsIllegalArgumentExceptionWhenRequestNotDifferent() {
        var token = TestUtils.basicToken(jwtBuilder);
        var allFieldsUpdate = ProfileUpdateRequest.builder()
                .firstName(TestUtils.TEST_PROFILE.getFirstName())
                .lastName(TestUtils.TEST_PROFILE.getLastName())
                .birthdate(TestUtils.TEST_PROFILE.getBirthdate())
                .build();
        var firstNameOnlyUpdate = ProfileUpdateRequest.builder()
                .firstName(TestUtils.TEST_PROFILE.getFirstName())
                .build();
        var lastNameOnlyUpdate = ProfileUpdateRequest.builder()
                .lastName(TestUtils.TEST_PROFILE.getLastName())
                .build();
        var birthdateOnlyUpdate = ProfileUpdateRequest.builder()
                .birthdate(TestUtils.TEST_PROFILE.getBirthdate())
                .build();
        when(userRepository.findByUsername(anyString()))
                .thenReturn(TestUtils.TEST_USER);
        assertThrows(IllegalArgumentException.class, () -> service.updateProfile(token, allFieldsUpdate));
        assertThrows(IllegalArgumentException.class, () -> service.updateProfile(token, firstNameOnlyUpdate));
        assertThrows(IllegalArgumentException.class, () -> service.updateProfile(token, lastNameOnlyUpdate));
        assertThrows(IllegalArgumentException.class, () -> service.updateProfile(token, birthdateOnlyUpdate));
    }

    @Test
    void updateProfile_ThrowsUsernameNotFoundException() {
        var generatedToken = TestUtils.basicToken(jwtBuilder);
        var updateRequest = ProfileUpdateRequest.builder()
                        .firstName("BobJim")
                        .build();
        when(userRepository.findByUsername(anyString())).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> service.updateProfile(generatedToken, updateRequest));

        verify(userRepository, times(1)).findByUsername(anyString());
    }
}
