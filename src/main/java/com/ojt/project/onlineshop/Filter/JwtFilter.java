package com.ojt.project.onlineshop.Filter;

import com.ojt.project.onlineshop.Util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;


@Component
public class JwtFilter extends OncePerRequestFilter implements Ordered {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public int getOrder() {
        return 1;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                    HttpServletResponse response, 
                                    FilterChain filterChain) 
            throws ServletException, IOException {
        
        // Bước 1: Lấy token từ header "Authorization"
        String authHeader = request.getHeader("Authorization");
        
        String token = null;
        String username = null;

        // Bước 2: Kiểm tra header có tồn tại và bắt đầu bằng "Bearer " không
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // Lấy token (bỏ phần "Bearer ")
            token = authHeader.substring(7);
            
            try {
                // Bước 3: Validate token trước khi lấy thông tin
                if (jwtUtil.validateToken(token)) {
                    // Token hợp lệ, lấy username từ token
                    username = jwtUtil.getUsernameFromToken(token);
                } else {
                    // Token không hợp lệ
                    filterChain.doFilter(request, response);
                    return;
                }
            } catch (Exception e) {
                filterChain.doFilter(request, response);
                return;
            }
        }

        // Bước 4: Nếu có username và chưa có authentication trong SecurityContext
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            String role = jwtUtil.getRoleFromToken(token);
            // Bước 5: Tạo authentication object với username và role
            SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);
            UsernamePasswordAuthenticationToken authToken = 
                new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    Collections.singletonList(authority)
                );
            // Bước 6: Set thông tin request vào authentication
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            // Bước 7: Lưu authentication vào SecurityContext
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
        // Bước 8: Tiếp tục filter chain (cho request đi tiếp đến controller)
        filterChain.doFilter(request, response);
    }
}
