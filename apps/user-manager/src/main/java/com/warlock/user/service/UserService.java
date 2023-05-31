package com.warlock.user.service;

import com.warlock.user.model.*;
import com.warlock.user.repository.ProfileRepository;
import com.warlock.user.repository.UserRepository;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.expression.AccessException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
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

    @Value("${user-service.token.expirationSeconds}")
    private long expirationSeconds;

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
        var user = fetchUser(username);
        return UserProfile.builder()
                .username(user.getUsername())
                .firstName(user.getProfile().getFirstName())
                .lastName(user.getProfile().getLastName())
                .birthdate(user.getProfile().getBirthdate())
                .registrationDate(user.getProfile().getRegistrationDate())
                .build();
    }

    public UserProfile updateProfile(String token, ProfileUpdateRequest request) throws AccessException {
        if (null == request.getBirthdate() && null == request.getFirstName() && null == request.getLastName()) {
            throw new IllegalArgumentException("At least one field must be updated");
        }
        var username = extractUsername(token);
        var newUser = updateUser(username, request);
        return UserProfile.builder()
                .username(newUser.getUsername())
                .firstName(newUser.getProfile().getFirstName())
                .lastName(newUser.getProfile().getLastName())
                .birthdate(newUser.getProfile().getBirthdate())
                .registrationDate(newUser.getProfile().getRegistrationDate())
                .build();
    }

    private String buildToken(String username) {
        log.info("Token expires in {} seconds", expirationSeconds);
        var expiryTime = Date.from(Instant.now().plusSeconds(expirationSeconds));
        return jwtBuilder
                .setSubject(username)
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS512, "secretKey")
                .setExpiration(expiryTime)
                .compact();
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

    private UserDocument fetchUser(String username) {
        var user = userRepository.findByUsername(username);
        if (null == user) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }

    private boolean profilesMatch(ProfileDocument oldProfile, ProfileUpdateRequest newProfile) {
        return (newProfile.getBirthdate() == null || oldProfile.getBirthdate().equals(newProfile.getBirthdate())) &&
                (newProfile.getFirstName() == null || oldProfile.getFirstName().equals(newProfile.getFirstName())) &&
                (newProfile.getLastName() == null || oldProfile.getLastName().equals(newProfile.getLastName()));
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

    private UserDocument updateUser(String username, ProfileUpdateRequest request) {
        var oldUser = fetchUser(username);
        var oldProfile = oldUser.getProfile();
        if (profilesMatch(oldProfile, request)) {
            throw new IllegalArgumentException("At least one field must be updated");
        }
        if (request.getFirstName() != null) {
            oldProfile.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            oldProfile.setLastName(request.getLastName());
        }
        if (request.getBirthdate() != null) {
            oldProfile.setBirthdate(request.getBirthdate());
        }
        var newProfile = profileRepository.save(oldProfile);

        oldUser.setProfile(newProfile);
        return userRepository.save(oldUser);
    }
}
