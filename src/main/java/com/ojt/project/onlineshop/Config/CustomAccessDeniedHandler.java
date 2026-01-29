package com.ojt.project.onlineshop.Config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ojt.project.onlineshop.Exception.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

//Xử lý lỗi 403 Forbidden

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, 
                     HttpServletResponse response,
                     AccessDeniedException accessDeniedException) throws IOException, ServletException {
        
        // Bước 1: Tạo ErrorResponse với thông tin lỗi
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setStatus(403); // Mã lỗi 403 Forbidden
        errorResponse.setMessage("Bạn không có quyền truy cập tài nguyên này.");
        errorResponse.setPath(request.getRequestURI()); // Đường dẫn API bị lỗi
        
        // Bước 2: Set headers cho response
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(403);
        
        // Bước 3: Convert ErrorResponse object sang JSON string
        ObjectMapper mapper = new ObjectMapper();
        String jsonResponse = mapper.writeValueAsString(errorResponse);
        
        // Bước 4: Gửi JSON response về client
        response.getWriter().write(jsonResponse);
    }
}
