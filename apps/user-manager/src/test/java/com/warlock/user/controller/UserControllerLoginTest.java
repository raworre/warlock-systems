package com.warlock.user.controller;


import com.warlock.user.TestUtils;
import com.warlock.user.model.LoginRequest;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Date;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerLoginTest extends UserControllerTest {
    @Test
    void login_ReturnsBadRequest() throws Exception {
        var emptyLoginRequest = LoginRequest.builder().build();
        var missingUsernameRequest =
                LoginRequest.builder().password(TestUtils.TEST_USER.getPassword()).build();
        var missingPasswordRequest =
                LoginRequest.builder().username(TestUtils.TEST_USER.getUsername()).build();

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
        var generatedToken = Jwts.builder()
                .setSubject(TestUtils.TEST_USER.getUsername())
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS512, "secretKey")
                .compact();
        when(service.login(any())).thenReturn(generatedToken);
        var loginRequest = LoginRequest.builder()
                .password(TestUtils.TEST_USER.getPassword())
                .username(TestUtils.TEST_USER.getUsername()).build();

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

        assertThat(sub).isEqualTo(TestUtils.TEST_USER.getUsername());

        var authHeader = result.getResponse().getHeader("Authorization");
        assertThat(authHeader).isEqualTo("Bearer " + token);
    }

    @Test
    public void login_UsernameNotFoundReturnsUnauthorized() throws Exception {
        var loginRequest = LoginRequest.builder()
                .password(TestUtils.TEST_USER.getPassword())
                .username(TestUtils.TEST_USER.getUsername()).build();
        when(service.login(any())).thenThrow(new UsernameNotFoundException(""));

        mockMvc.perform(post("/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void login_BadPasswordReturnsUnauthorized() throws Exception {
        var loginRequest = LoginRequest.builder()
                .password(TestUtils.TEST_USER.getPassword())
                .username(TestUtils.TEST_USER.getUsername()).build();
        when(service.login(any())).thenThrow(new BadCredentialsException(""));

        mockMvc.perform(post("/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }
}
