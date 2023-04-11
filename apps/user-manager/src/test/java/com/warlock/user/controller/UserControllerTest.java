package com.warlock.user.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.warlock.user.model.LoginRequest;
import com.warlock.user.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

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
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private UserService service;

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

        verifyNoInteractions(service);
    }

    @Test
    void login_ReturnsOk() throws Exception {
        var loginUsername = "hrothgar.warlock";
        var generatedToken = Jwts.builder()
                .setSubject(loginUsername)
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS512, "secretKey")
                .compact();
        when(service.login(any())).thenReturn(generatedToken);
        var loginRequest = LoginRequest.builder()
                .password("username")
                .username(loginUsername).build();

        var result = mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andReturn();

        var token = (String) mapper.readValue(result.getResponse().getContentAsString(), Map.class).get("token");

        var sub = Jwts.parser()
                .setSigningKey("secretKey")
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        assertThat(sub).isEqualTo(loginUsername);
    }
}
