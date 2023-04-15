package com.warlock.user.service;

import com.warlock.user.repository.ProfileRepository;
import com.warlock.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
public class UserServiceTest {
    @Autowired
    UserService service;

    @MockBean
    UserRepository userRepository;

    @MockBean
    ProfileRepository profileRepository;
}
