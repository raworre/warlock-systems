package com.warlock.user.service;

import com.warlock.user.model.LoginRequest;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    private UserService service;

    @Test
    public void login_ShouldReturnToken() {
        var loginUser = "hrothgar.warlock";
        var loginRequest = LoginRequest.builder()
                .username(loginUser)
                .password("password").build();
        var token = service.login(loginRequest);

        var username = Jwts.parser()
                .setSigningKey("secretKey")
                .parseClaimsJws(token)
                .getBody()
                .getSubject();

        assertThat(username).isEqualTo(loginUser);
    }
}
