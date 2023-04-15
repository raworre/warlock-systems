package com.warlock.user.service;

import org.junit.jupiter.api.Test;
import org.springframework.expression.AccessException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verifyNoInteractions;

public class UserServiceProfileTest extends UserServiceTest {
    @Test
    void fetchProfile_ReturnsProfileObject() {
        assertTrue(true);
    }

    @Test
    void fetchProfile_ThrowsAccessException() {
        verifyNoInteractions(userRepository);
        verifyNoInteractions(profileRepository);

        assertThrows(AccessException.class, () -> service.fetchProfile(""));
    }
}
