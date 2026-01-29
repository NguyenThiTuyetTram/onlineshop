package com.ojt.project.onlineshop.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

// Cấu hình CORS (Cross-Origin Resource Sharing)

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        
        // Bước 1: Cho phép các domain (origin) được gọi API
        config.setAllowedOrigins(Arrays.asList(
            "http://localhost:3000",  // React
            "http://localhost:8080",  // Vue
            "http://localhost:5173"   // Vite
        ));
        
        // Bước 2: Cho phép các HTTP methods
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        
        // Bước 3: Cho phép tất cả headers
        config.setAllowedHeaders(Arrays.asList("*"));
        
        // Bước 4: Cho phép gửi credentials (cookies, authorization headers)
        config.setAllowCredentials(true);
        
        // Bước 5: Thời gian cache preflight request (1 giờ = 3600 giây)
        config.setMaxAge(3600L);
        
        // Bước 6: Áp dụng cấu hình cho tất cả các path
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
