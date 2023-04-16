package com.warlock.user;

import com.warlock.user.model.ProfileDocument;
import com.warlock.user.model.UserDocument;

import java.time.LocalDate;

public class TestUtils {
    public static final ProfileDocument TEST_PROFILE = ProfileDocument.builder()
            .registrationDate(LocalDate.now())
            .firstName("Test")
            .lastName("User")
            .birthdate(LocalDate.parse("1999-06-18"))
            .build();

    public static final UserDocument TEST_USER = UserDocument.builder()
            .username("test.username")
            .password("test.password")
            .profile(TEST_PROFILE)
            .build();
}
