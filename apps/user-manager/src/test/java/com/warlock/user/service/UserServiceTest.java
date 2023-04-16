package com.warlock.user.service;

import com.warlock.user.repository.ProfileRepository;
import com.warlock.user.repository.UserRepository;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    UserService service;

    @Autowired
    JwtParser jwtParser;

    @Autowired
    JwtBuilder jwtBuilder;

    @MockBean
    UserRepository userRepository;

    @MockBean
    ProfileRepository profileRepository;
}
