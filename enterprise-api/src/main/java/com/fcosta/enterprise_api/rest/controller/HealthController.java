package com.fcosta.enterprise_api.rest.controller;

import java.time.LocalDateTime;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HealthController {
    @GetMapping("/")
    public Map<String, Object> root() {
        return Map.of(
                "application", "Supplier Management API",
                "status", "running",
                "timestamp", LocalDateTime.now()
        );
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "UP");
    }
    
}
