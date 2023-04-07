package com.warlock.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.ResponseEntity.ok;

@Controller
@Slf4j
public class UserController {
    @GetMapping("/login")
    public ResponseEntity<Map<String, String>> login() {
        log.info("Login called...");
        return ok(new HashMap<>() {{
            put("token", "You really thought you'd get a real token? CHUMP!");
        }});
    }
}
