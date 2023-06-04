package com.warlock.user.controller;

import com.warlock.user.TestUtils;
import com.warlock.user.model.ProfileUpdateRequest;
import com.warlock.user.model.UserProfile;
import org.junit.jupiter.api.Test;
import org.springframework.expression.AccessException;
import org.springframework.http.MediaType;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
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

    @Test
    void updateProfile_ReturnsUnauthorizedWhenUserNotFound() throws Exception {
        var profileUpdateRequest = ProfileUpdateRequest.builder().build();
        when(service.updateProfile(anyString(), any()))
                .thenThrow(new AccessException(""));

        mockMvc.perform(post("/profile/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(profileUpdateRequest))
                        .header("Authorization", TestUtils.basicToken(jwtBuilder)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void updateProfile_ReturnsOk() throws Exception {
        var request = ProfileUpdateRequest.builder()
                .firstName("JimBob")
                .lastName("Chucklefuck")
                .build();
        when(service.updateProfile(anyString(), eq(request)))
                .thenReturn(TestUtils.UPDATED_PROFILE);

        var response = mockMvc.perform(post("/profile/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request))
                .header("Authorization", TestUtils.basicToken(jwtBuilder)))
                .andExpect(status().isOk())
                .andReturn();

        var newProfile = mapper.readValue(response.getResponse().getContentAsString(), UserProfile.class);

        verify(service, times(1))
                .updateProfile(anyString(), eq(request));

        assertNotNull(newProfile);
        assertThat(newProfile.getFirstName()).isEqualTo(TestUtils.UPDATED_PROFILE.getFirstName());
        assertThat(newProfile.getLastName()).isEqualTo(TestUtils.UPDATED_PROFILE.getLastName());
    }
}
