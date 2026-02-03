package com.ojt.project.onlineshop.Config;

import com.ojt.project.onlineshop.Filter.JwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
// Import mới cho CORS
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Autowired
        private JwtFilter jwtFilter;

        // @Autowired
        // private CorsFilter corsFilter; // XÓA HOẶC COMMENT DÒNG NÀY (Ta sẽ cấu hình
        // bên dưới)

        @Autowired
        private CustomAuthenticationEntryPoint authenticationEntryPoint;

        @Autowired
        private CustomAccessDeniedHandler accessDeniedHandler;

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                http
                                // 1. Tắt CSRF (Bắt buộc với JWT)
                                .csrf(AbstractHttpConfigurer::disable)

                                // 2. KÍCH HOẠT CORS: Sử dụng cấu hình từ bean corsConfigurationSource bên dưới
                                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                                .authorizeHttpRequests(auth -> auth
                                                // Cho phép truy cập Swagger và Docs
                                                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**",
                                                                "/v3/api-docs.yaml")
                                                .permitAll()

                                                // Cho phép Auth và Public API
                                                .requestMatchers("/api/auth/**", "/api/public/**").permitAll()

                                                // Admin API (Tạm thời để permitAll để em test, sau này nên đổi thành
                                                // hasRole("ADMIN"))
                                                .requestMatchers("/api/admin/**").permitAll()

                                                // Các request còn lại bắt buộc phải có Token
                                                .anyRequest().authenticated())

                                .exceptionHandling(exceptions -> exceptions
                                                .authenticationEntryPoint(authenticationEntryPoint)
                                                .accessDeniedHandler(accessDeniedHandler))

                                // Thêm JWT Filter trước UsernamePasswordAuthenticationFilter
                                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }

        // 3. BEAN CẤU HÌNH CORS (QUAN TRỌNG NHẤT)
        @Bean
        public CorsConfigurationSource corsConfigurationSource() {
                CorsConfiguration configuration = new CorsConfiguration();

                // DANH SÁCH DOMAIN ĐƯỢC PHÉP TRUY CẬP
                configuration.setAllowedOrigins(List.of(
                                "https://onlineshop-i0m4.onrender.com", // Link Frontend trên Render
                                "http://localhost:5173", // Link Frontend chạy Local (Vite)
                                "http://localhost:3000" // Link Frontend chạy Local (CRA - dự phòng)
                ));

                configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
                configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "x-auth-token"));
                configuration.setAllowCredentials(true); // Cho phép gửi cookie/credential nếu cần

                UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
                source.registerCorsConfiguration("/**", configuration);
                return source;
        }
}