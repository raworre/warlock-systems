package com.warlock.user.service;

import com.warlock.user.model.RegistrationRequest;
import com.warlock.user.model.UserDocument;
import com.warlock.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserServiceRegisterTest {
    @Autowired
    private UserService service;

    @MockBean
    private UserRepository repository;

    @Test
    public void register_ShouldReturnToken() {
        when(repository.findByUsername(any())).thenReturn(null);
        var registrationRequest = RegistrationRequest.builder().build();

        var token = service.register(registrationRequest);

        assertThat(token).isBlank();
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
        when(repository.findByUsername(any())).thenReturn(userDocument);

        assertThrows(UsernameAlreadyExistsException.class, () -> service.register(registerRequest));
        verify(repository, times(1))
                .findByUsername(registerRequest.getUsername());
    }
}
