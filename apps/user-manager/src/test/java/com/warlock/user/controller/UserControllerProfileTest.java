package com.warlock.user.controller;

import com.warlock.user.TestUtils;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;
import org.springframework.expression.AccessException;

import java.util.Date;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class UserControllerProfileTest extends UserControllerTest {
    @Test
    void profile_ReturnsOk() throws Exception {
        var generatedToken = jwtBuilder
                .setSubject(TestUtils.TEST_USER.getUsername())
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS512, "secretKey")
                .compact();
        when(service.fetchProfile(anyString())).thenReturn(null);

        mockMvc.perform(get("/profile").header("Authorization", generatedToken))
                .andExpect(status().isOk());

        verify(service, times(1)).fetchProfile(anyString());
    }

    @Test
    void profile_ReturnsUnauthorizedWhenUserNotFound() throws Exception {
        when(service.fetchProfile(anyString())).thenThrow(new AccessException(""));

        mockMvc.perform(get("/profile").header("Authorization", ""))
                .andExpect(status().isUnauthorized());
    }
}
