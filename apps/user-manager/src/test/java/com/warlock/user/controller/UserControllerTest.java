package com.warlock.user.controller;


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
        assertTrue(response.getBody().containsKey("token"));
        assertTrue(response.getBody().get("token").contains("CHUMP"));
    }
}
