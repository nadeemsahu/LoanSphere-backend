package com.loan.app.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

/**
 * Health check endpoint.
 * Used by:
 *  - Render's health check pings (keeps the service alive)
 *  - Frontend "is backend awake?" pre-flight before login
 *
 * Route: GET /api/health
 */
@RestController
@RequestMapping("/api")
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        return ResponseEntity.ok(Map.of(
            "status", "UP",
            "service", "LoanSphere Backend",
            "timestamp", Instant.now().toString()
        ));
    }
}
