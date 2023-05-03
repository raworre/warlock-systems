package com.warlock.user.controller;

import com.warlock.user.TestUtils;
import com.warlock.user.model.ProfileUpdateRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerUpdateProfileTest extends UserControllerTest {
    @Test
    void updateProfile_ReturnsBadRequest() throws Exception {
        var profileUpdateRequest = ProfileUpdateRequest.builder().build();
        when(service.updateProfile(anyString(), any()))
                .thenThrow(new IllegalArgumentException(""));
        mockMvc.perform(post("/profile/update")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(profileUpdateRequest))
                    .header("Authorization", TestUtils.basicToken(jwtBuilder)))
                .andExpect(status().isBadRequest());
    }
}
