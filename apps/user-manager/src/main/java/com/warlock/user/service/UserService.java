package com.warlock.user.service;

import com.warlock.user.model.LoginRequest;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
public class UserService {
    public String login(LoginRequest loginRequest) {
        log.info("Login service called");
        return Jwts.builder()
                .setSubject(loginRequest.getUsername())
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS512, "secretKey")
                .compact();
    }
}
