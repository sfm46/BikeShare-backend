package com.bikeshare.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                // In production, restrict allowedOrigins to specific domains
                registry.addMapping("/**")
                        .allowedOrigins(System.getenv("CORS_ALLOWED_ORIGINS") != null ? System.getenv("CORS_ALLOWED_ORIGINS").split(",") : new String[]{"*"})
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*");
            }
        };
    }
}
