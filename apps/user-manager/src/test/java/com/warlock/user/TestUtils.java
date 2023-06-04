package com.warlock.user;

import com.warlock.user.model.ProfileDocument;
import com.warlock.user.model.UserDocument;
import com.warlock.user.model.UserProfile;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.SignatureAlgorithm;

import java.time.LocalDate;
import java.util.Date;

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

    public static final ProfileDocument UPDATED_NAME_PROFILE = ProfileDocument.builder()
            .registrationDate(TEST_PROFILE.getRegistrationDate())
            .firstName("JimBob")
            .lastName("Chucklefuck")
            .birthdate(TEST_PROFILE.getBirthdate())
            .build();

    public static final UserDocument UPDATED_FIRST_NAME_USER = UserDocument.builder()
            .username("test.username")
            .password("test.password")
            .profile(UPDATED_NAME_PROFILE)
            .build();

    public static final UserProfile UPDATED_PROFILE = UserProfile.builder()
            .registrationDate(LocalDate.now())
            .birthdate(UPDATED_NAME_PROFILE.getBirthdate())
            .firstName(UPDATED_NAME_PROFILE.getFirstName())
            .lastName(UPDATED_NAME_PROFILE.getLastName())
            .username(TEST_USER.getUsername())
            .build();

    public static String basicToken(JwtBuilder jwtBuilder) {
        return jwtBuilder
                .setSubject(TestUtils.TEST_USER.getUsername())
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS512, "secretKey")
                .compact();
    }

    public static String noSubToken(JwtBuilder jwtBuilder) {
        return jwtBuilder
                .setSubject("")
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS512, "secretKey")
                .compact();
    }
}
