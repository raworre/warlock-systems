package com.warlock.user.controller;

import com.warlock.user.model.Profile;
import com.warlock.user.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.ResponseEntity.ok;

@Controller
@Slf4j
public class UserController {
    @GetMapping("/login")
    public ResponseEntity<Map<String, Object>> login() {
        log.info("Login called...");
        var profile = new Profile();
        profile.setId("2b");
        profile.setBirthdate(LocalDate.parse("1981-06-18"));
        profile.setFirstName("Hrothgar");
        profile.setLastName("Warlock");
        var user = new User();
        user.setId("1a");
        user.setProfile(profile);
        user.setUsername("hrothgar.warlock");
        user.setPassword("p4ssw0rd");
        return ok(new HashMap<>() {{
            put("token", "You really thought you'd get a real token? CHUMP!");
            put("user", user);
        }});
    }
}
