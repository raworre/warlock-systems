package com.warlock.user.controller;


import com.warlock.user.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class UserControllerTest {
    @Autowired
    UserController userController;

    @Test
    public void login_ReturnsSuccess() {
        var response = userController.login();
        assertTrue(response.hasBody());
        assertThat(response.getBody()).isNotNull();
        var responseBody = response.getBody();
        assertTrue(responseBody.containsKey("token"));
        assertTrue(responseBody.containsKey("user"));
        var token = (String) responseBody.get("token");
        assertThat(token).contains("CHUMP");
        var user = (User) responseBody.get("user");
        assertThat(user.getUsername()).isEqualTo("hrothgar.warlock");
        assertThat(user.getId()).isEqualTo("1a");
        assertThat(user.getPassword()).isEqualTo("p4ssw0rd");
        var profile = user.getProfile();
        assertThat(profile.getId()).isEqualTo("2b");
        assertThat(profile.getBirthdate()).isEqualTo("1981-06-18");
        assertThat(profile.getFirstName()).isEqualTo("Hrothgar");
        assertThat(profile.getLastName()).isEqualTo("Warlock");
    }
}
