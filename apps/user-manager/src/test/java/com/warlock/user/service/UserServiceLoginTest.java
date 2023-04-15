package com.warlock.user.service;

import com.warlock.user.model.LoginRequest;
import com.warlock.user.model.UserDocument;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class UserServiceLoginTest extends UserServiceTest {
    @Test
    public void login_ShouldReturnToken() {
        var loginUser = "hrothgar.warlock";
        var loginRequest = LoginRequest.builder()
                .username(loginUser)
                .password("password").build();
        var userDocument = UserDocument.builder()
                .username(loginRequest.getUsername())
                .password("$2a$10$7ZF.muu7R1zRMJVY2e1VoeFocTwn5BYLXYwWHPF6a49A4zw1VdHy6")
                .build();
        when(userRepository.findByUsername(anyString())).thenReturn(userDocument);
        var token = service.login(loginRequest);

        var username = Jwts.parser()
                .setSigningKey("secretKey")
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        assertThat(username).isEqualTo(loginUser);
        verify(userRepository, times(1))
                .findByUsername(loginRequest.getUsername());
    }

    @Test
    public void login_UserNotFoundShouldThrowException() {
        var loginUser = "hrothgar.warlock";
        var loginRequest = LoginRequest.builder()
                .username(loginUser)
                .password("password").build();
        when(userRepository.findByUsername(anyString())).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> service.login(loginRequest));
        verify(userRepository, times(1))
                .findByUsername(loginRequest.getUsername());
    }

    @Test
    public void login_InvalidPasswordShouldThrowException() {
        var loginUser = "hrothgar.warlock";
        var loginRequest = LoginRequest.builder()
                .username(loginUser)
                .password("password").build();
        var userDocument = UserDocument.builder()
                .username(loginRequest.getUsername())
                .password("")
                .build();
        when(userRepository.findByUsername(anyString())).thenReturn(userDocument);

        assertThrows(BadCredentialsException.class, () -> service.login(loginRequest));
        verify(userRepository, times(1))
                .findByUsername(loginRequest.getUsername());
    }
}
