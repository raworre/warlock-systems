package com.warlock.user.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.warlock.user.model.LoginRequest;
import com.warlock.user.model.RegistrationRequest;
import com.warlock.user.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Date;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerRegisterTest {
    private static final LocalDate BIRTHDATE = LocalDate.parse("1981-06-18");
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private UserService service;

    @Test
    void register_ReturnsBadRequest() throws Exception {
        var emptyRegisterRequest = RegistrationRequest.builder().build();
        var missingUsernameRequest = RegistrationRequest.builder()
                .password("password")
                .birthdate(BIRTHDATE)
                .firstName("Hrothgar")
                .lastName("Warlock")
                .build();
        var missingPasswordRequest = RegistrationRequest.builder()
                .username("username")
                .birthdate(BIRTHDATE)
                .firstName("Hrothgar")
                .lastName("Warlock")
                .build();
        var missingFirstNameRequest = RegistrationRequest.builder()
                .username("username")
                .password("password")
                .birthdate(BIRTHDATE)
                .lastName("Warlock")
                .build();
        var missingLastNameRequest = RegistrationRequest.builder()
                .username("username")
                .password("password")
                .birthdate(BIRTHDATE)
                .firstName("Hrothgar")
                .build();
        var missingBirthdateRequest = RegistrationRequest.builder()
                .username("username")
                .password("password")
                .firstName("Hrothgar")
                .lastName("Warlock")
                .build();

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(emptyRegisterRequest)))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(missingUsernameRequest)))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(missingPasswordRequest)))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(missingFirstNameRequest)))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(missingLastNameRequest)))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(missingBirthdateRequest)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(service);
    }

    @Test
    void register_ReturnsOk() throws Exception {
        var registerRequest = RegistrationRequest.builder()
                .username("username")
                .password("password")
                .firstName("Hrothgar")
                .lastName("Warlock")
                .birthdate(BIRTHDATE)
                .build();

        var result = mockMvc.perform(post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk())
                .andReturn();

        var token = (String) mapper.readValue(result.getResponse().getContentAsString(), Map.class).get("token");

        assertThat(token).isNullOrEmpty();
    }
}
