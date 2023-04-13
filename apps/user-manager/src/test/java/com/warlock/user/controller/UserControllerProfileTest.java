package com.warlock.user.controller;

import com.warlock.user.TestUtils;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class UserControllerProfileTest extends UserControllerTest{
    @Test
    void profile_ReturnsOk() throws Exception {
        var generatedToken = Jwts.builder()
                .setSubject(TestUtils.TEST_USER.getUsername())
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS512, "secretKey")
                .compact();

        mockMvc.perform(get("/profile").header("Authorization", generatedToken))
                .andExpect(status().isOk());

        verifyNoInteractions(service);
    }
}
