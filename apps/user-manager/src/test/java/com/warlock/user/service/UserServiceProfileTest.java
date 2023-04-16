package com.warlock.user.service;

import com.warlock.user.TestUtils;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;
import org.springframework.expression.AccessException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class UserServiceProfileTest extends UserServiceTest {
    @Test
    void fetchProfile_ReturnsProfileObject() throws AccessException {
        var generatedToken = jwtBuilder
                .setSubject(TestUtils.TEST_USER.getUsername())
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS512, "secretKey")
                .compact();
        when(userRepository.findByUsername(anyString()))
                .thenReturn(TestUtils.TEST_USER);

        var profile = service.fetchProfile(generatedToken);

        verify(userRepository, times(1)).findByUsername(anyString());

        assertNotNull(profile);
        assert(profile.getUsername()).equals(TestUtils.TEST_USER.getUsername());
        assert(profile.getFirstName()).equals(TestUtils.TEST_PROFILE.getFirstName());
        assert(profile.getLastName()).equals(TestUtils.TEST_PROFILE.getLastName());
        assert(profile.getBirthdate()).equals(TestUtils.TEST_PROFILE.getBirthdate());
        assert(profile.getRegistrationDate()).equals(TestUtils.TEST_PROFILE.getRegistrationDate());
    }

    @Test
    void fetchProfile_ThrowsAccessException() {
        var generatedToken = jwtBuilder
                .setSubject("")
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS512, "secretKey")
                .compact();

        assertThrows(AccessException.class, () -> service.fetchProfile(""));
        assertThrows(AccessException.class, () -> service.fetchProfile("209alpskdhf9q82yh34akdfvnm;a"));
        assertThrows(AccessException.class, () -> service.fetchProfile("part.one.two"));
        assertThrows(AccessException.class, () -> service.fetchProfile(generatedToken));

        verifyNoInteractions(userRepository);
        verifyNoInteractions(profileRepository);
    }

    @Test
    void fetchProfile_ThrowsUsernameNotFoundException() {
        var generatedToken = jwtBuilder
                .setSubject(TestUtils.TEST_USER.getUsername())
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS512, "secretKey")
                .compact();
        when(userRepository.findByUsername(anyString())).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> service.fetchProfile(generatedToken));

        verify(userRepository, times(1)).findByUsername(anyString());
    }
}
