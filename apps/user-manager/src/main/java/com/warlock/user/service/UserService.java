package com.warlock.user.service;

import com.warlock.user.model.LoginRequest;
import com.warlock.user.model.ProfileDocument;
import com.warlock.user.model.RegistrationRequest;
import com.warlock.user.model.UserDocument;
import com.warlock.user.repository.ProfileRepository;
import com.warlock.user.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;

    public String login(LoginRequest loginRequest) {
        var user = userRepository.findByUsername(loginRequest.getUsername());
        if (null == user) {
            throw new UsernameNotFoundException("Invalid credentials");
        }

        var passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        return buildToken(loginRequest.getUsername());
    }

    public String register(RegistrationRequest registrationRequest) {
        var user = userRepository.findByUsername(registrationRequest.getUsername());
        if (null != user) {
            throw new UsernameAlreadyExistsException("Username already exists");
        }

        var savedUser = saveUser(registrationRequest);

        return buildToken(savedUser.getUsername());
    }

    private UserDocument saveUser(RegistrationRequest registrationRequest) {
        var profileDoc = ProfileDocument.builder()
                .firstName(registrationRequest.getFirstName())
                .lastName(registrationRequest.getLastName())
                .birthdate(registrationRequest.getBirthdate())
                .registrationDate(LocalDate.now())
                .build();
        profileRepository.save(profileDoc);

        var userDoc = UserDocument.builder()
                .username(registrationRequest.getUsername())
                .password(new BCryptPasswordEncoder().encode(registrationRequest.getPassword()))
                .profile(profileDoc)
                .build();
        return userRepository.save(userDoc);
    }

    private String buildToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS512, "secretKey")
                .compact();
    }
}
