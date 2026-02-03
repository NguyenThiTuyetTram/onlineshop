package com.ojt.project.onlineshop.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                // 1. Tắt CSRF (Rất quan trọng với API RESTful stateless)
                                .csrf(AbstractHttpConfigurer::disable)

                                // 2. Kích hoạt cấu hình CORS từ bean bên dưới
                                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                                // 3. Cấu hình phân quyền (Authorize)
                                .authorizeHttpRequests(auth -> auth
                                                // Cho phép truy cập tự do vào các API auth và public
                                                .requestMatchers("/api/auth/**", "/api/public/**", "/swagger-ui/**",
                                                                "/v3/api-docs/**")
                                                .permitAll()
                                                // Các request khác phải đăng nhập
                                                .anyRequest().authenticated())
                // ... (Giữ nguyên các cấu hình filter JWT khác của em nếu có)
                ;

                return http.build();
        }

        // Bean cấu hình CORS chi tiết
        @Bean
        public CorsConfigurationSource corsConfigurationSource() {
                CorsConfiguration configuration = new CorsConfiguration();

                // QUAN TRỌNG: Cho phép đúng domain Frontend của em
                // Không được có dấu gạch chéo / ở cuối string
                configuration.setAllowedOrigins(
                                List.of("https://onlineshop-i0m4.onrender.com", "http://localhost:5173"));

                configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "x-auth-token"));
                configuration.setAllowCredentials(true); // Cho phép gửi cookie/credential nếu cần

                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);
                return source;
        }
}