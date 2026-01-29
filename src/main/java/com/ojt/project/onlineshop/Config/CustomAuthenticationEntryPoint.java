package com.ojt.project.onlineshop.Config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ojt.project.onlineshop.Exception.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

// Xử lý lỗi 401 Unauthorized

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, 
                        HttpServletResponse response,
                        AuthenticationException authException) throws IOException, ServletException {
        
        // Bước 1: Tạo ErrorResponse với thông tin lỗi
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(401); // Mã lỗi 401 Unauthorized
        errorResponse.setMessage("Xác thực thất bại. Vui lòng đăng nhập lại.");
        errorResponse.setPath(request.getRequestURI()); // Đường dẫn API bị lỗi
        
        // Bước 2: Set headers cho response
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(401); // Set status code 401
        
        // Bước 3: Convert ErrorResponse object sang JSON string
        ObjectMapper mapper = new ObjectMapper();
        String jsonResponse = mapper.writeValueAsString(errorResponse);
        
        // Bước 4: Gửi JSON response về client
        response.getWriter().write(jsonResponse);
    }
}
