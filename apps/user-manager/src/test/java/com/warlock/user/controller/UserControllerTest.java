package com.warlock.user.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.warlock.user.model.LoginRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void login_ReturnsBadRequest() throws Exception {
        var emptyLoginRequest = LoginRequest.builder().build();
        var missingUsernameRequest =
                LoginRequest.builder().password("password").build();
        var missingPasswordRequest =
                LoginRequest.builder().username("username").build();

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(emptyLoginRequest)))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(missingUsernameRequest)))
                .andExpect(status().isBadRequest());

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(missingPasswordRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_ReturnsOk() throws Exception {
        var loginRequest = LoginRequest.builder()
                .password("username")
                .username("password").build();

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("Suck it"));
    }
}
