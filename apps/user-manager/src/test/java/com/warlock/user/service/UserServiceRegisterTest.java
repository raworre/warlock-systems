package com.warlock.user.service;

import com.warlock.user.model.RegistrationRequest;
import com.warlock.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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
}
