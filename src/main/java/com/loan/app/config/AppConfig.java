package com.loan.app.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppConfig {

    // Extra origins can be injected via CORS_ALLOWED_ORIGINS env var on Render
    // e.g. CORS_ALLOWED_ORIGINS=https://myapp.vercel.app
    @Value("${cors.allowed-origins:}")
    private String extraAllowedOrigins;

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper;
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                // Build allowed origins list — always include local dev + Vercel prod
                java.util.List<String> origins = new java.util.ArrayList<>(java.util.Arrays.asList(
                    "http://localhost:3000",
                    "http://localhost:5173",
                    "https://loansphere-one.vercel.app",
                    "https://www.loansphere-one.vercel.app"
                ));

                // Also accept any additional origins set via the CORS_ALLOWED_ORIGINS env var
                if (extraAllowedOrigins != null && !extraAllowedOrigins.isBlank()) {
                    for (String origin : extraAllowedOrigins.split(",")) {
                        String trimmed = origin.trim();
                        if (!trimmed.isEmpty()) origins.add(trimmed);
                    }
                }

                registry.addMapping("/**")
                        .allowedOrigins(origins.toArray(new String[0]))
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD")
                        .allowedHeaders("*")
                        .exposedHeaders("Content-Type", "Authorization")
                        .allowCredentials(false) // false = compatible with wildcard headers; set true only if cookies needed
                        .maxAge(3600);
            }
        };
    }
}
