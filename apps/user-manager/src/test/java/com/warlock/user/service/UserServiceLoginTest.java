package com.warlock.user.service;

import com.warlock.user.model.LoginRequest;
import com.warlock.user.model.UserDocument;
import com.warlock.user.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceLoginTest {
    @Autowired
    private UserService service;

    @MockBean
    private UserRepository repository;

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
        when(repository.findByUsername(anyString())).thenReturn(userDocument);
        var token = service.login(loginRequest);

        var username = Jwts.parser()
                .setSigningKey("secretKey")
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        assertThat(username).isEqualTo(loginUser);
        verify(repository, times(1))
                .findByUsername(loginRequest.getUsername());
    }

    @Test
    public void login_UserNotFoundShouldThrowException() {
        var loginUser = "hrothgar.warlock";
        var loginRequest = LoginRequest.builder()
                .username(loginUser)
                .password("password").build();
        when(repository.findByUsername(anyString())).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> service.login(loginRequest));
        verify(repository, times(1))
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
        when(repository.findByUsername(anyString())).thenReturn(userDocument);

        assertThrows(BadCredentialsException.class, () -> service.login(loginRequest));
        verify(repository, times(1))
                .findByUsername(loginRequest.getUsername());
    }
}
