package com.warlock.user.service;

import com.warlock.user.model.LoginRequest;
import com.warlock.user.model.RegistrationRequest;
import com.warlock.user.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public String login(LoginRequest loginRequest) {
        var user = userRepository.findByUsername(loginRequest.getUsername());
        if (null == user) {
            throw new UsernameNotFoundException("Invalid credentials");
        }

        var passwordEncoder = new BCryptPasswordEncoder();
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        return Jwts.builder()
                .setSubject(loginRequest.getUsername())
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS512, "secretKey")
                .compact();
    }

    public String register(RegistrationRequest registrationRequest) {
        var user = userRepository.findByUsername(registrationRequest.getUsername());
        if (null != user) {
            throw new UsernameAlreadyExistsException("Username already exists");
        }
        return "";
    }
}
