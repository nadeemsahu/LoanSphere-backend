package com.loan.app.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DbInitConfig implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) {
        log.info("Checking database schema and applying necessary updates...");
        try {
            // Attempt to increase column lengths for Enum-mapped strings
            jdbcTemplate.execute("ALTER TABLE loans ALTER COLUMN status TYPE VARCHAR(20)");
            jdbcTemplate.execute("ALTER TABLE users ALTER COLUMN role TYPE VARCHAR(20)");
            jdbcTemplate.execute("ALTER TABLE users ALTER COLUMN status TYPE VARCHAR(20)");
            log.info("Database schema updated successfully.");
        } catch (Exception e) {
            log.warn("Database schema update check finished with message: {}", e.getMessage());
        }
    }
}
