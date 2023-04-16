package com.warlock.user.service;

import com.warlock.user.model.*;
import com.warlock.user.repository.ProfileRepository;
import com.warlock.user.repository.UserRepository;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.AccessException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final JwtParser jwtParser;
    private final JwtBuilder jwtBuilder;

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

    public UserProfile fetchProfile(String token) throws AccessException {
        var username = extractUsername(token);
        var user = userRepository.findByUsername(username);
        if (null == user) {
            throw new UsernameNotFoundException("User not found");
        }
        return UserProfile.builder()
                .username(user.getUsername())
                .firstName(user.getProfile().getFirstName())
                .lastName(user.getProfile().getLastName())
                .birthdate(user.getProfile().getBirthdate())
                .registrationDate(user.getProfile().getRegistrationDate())
                .build();
    }

    private String extractUsername(String token) throws AccessException {
        try {
            var username = jwtParser
                    .setSigningKey("secretKey")
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
            if (null == username || username.isEmpty() || username.isBlank()) {
                throw new AccessException("");
            }
            return username;
        } catch (IllegalArgumentException | MalformedJwtException ex) {
            log.error("Provided JWT is invalid");
            throw new AccessException("");
        }
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
        return jwtBuilder
                .setSubject(username)
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS512, "secretKey")
                .compact();
    }
}
