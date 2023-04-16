package com.warlock.user.service;

import com.warlock.user.model.ProfileDocument;
import com.warlock.user.model.RegistrationRequest;
import com.warlock.user.model.UserDocument;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class UserServiceRegisterTest extends UserServiceTest {
    @Test
    public void register_ShouldReturnToken() {
        var registerRequest = RegistrationRequest.builder()
                .username("username")
                .password("password")
                .firstName("First")
                .lastName("Last")
                .birthdate(LocalDate.parse("1981-06-18"))
                .build();
        var profileDocument = ProfileDocument.builder()
                .firstName("First")
                .lastName("Last")
                .birthdate(registerRequest.getBirthdate())
                .build();
        var userDocument = UserDocument.builder()
                .username(registerRequest.getUsername())
                .password("$2a$10$7ZF.muu7R1zRMJVY2e1VoeFocTwn5BYLXYwWHPF6a49A4zw1VdHy6")
                .profile(profileDocument)
                .build();
        when(userRepository.findByUsername(any())).thenReturn(null);
        when(profileRepository.save(any())).thenReturn(profileDocument);
        when(userRepository.save(any())).thenReturn(userDocument);


        var token = service.register(registerRequest);

        var username = Jwts.parser()
                .setSigningKey("secretKey")
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        assertThat(username).isEqualTo(registerRequest.getUsername());
    }

    @Test
    void register_UserExistsThrowException() {
        var registerRequest = RegistrationRequest.builder()
                .username("username")
                .password("password")
                .firstName("First")
                .lastName("Last")
                .birthdate(LocalDate.parse("1981-06-18"))
                .build();
        var userDocument = UserDocument.builder()
                .username(registerRequest.getUsername())
                .password("$2a$10$7ZF.muu7R1zRMJVY2e1VoeFocTwn5BYLXYwWHPF6a49A4zw1VdHy6")
                .build();
        when(userRepository.findByUsername(any())).thenReturn(userDocument);

        assertThrows(UsernameAlreadyExistsException.class, () -> service.register(registerRequest));
        verify(userRepository, times(1))
                .findByUsername(registerRequest.getUsername());
    }
}
