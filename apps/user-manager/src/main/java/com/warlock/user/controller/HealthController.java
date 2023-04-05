package com.warlock.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import static org.springframework.http.ResponseEntity.ok;

@Controller
@Slf4j
public class HealthController implements HealthIndicator {
    @GetMapping("/health")
    public ResponseEntity<Health> baseCheck() {
        log.info("Health check requested");
        return ok(health());
    }

    @Override
    public Health health() {
        return Health.up().build();
    }
}
