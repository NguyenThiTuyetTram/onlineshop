package com.ojt.project.onlineshop.Service;

import com.ojt.project.onlineshop.Dto.LoginRequest;
import com.ojt.project.onlineshop.Dto.LoginResponse;
import com.ojt.project.onlineshop.Entity.User;
import com.ojt.project.onlineshop.Repository.UserRepository;
import com.ojt.project.onlineshop.Util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.ojt.project.onlineshop.Dto.RegisterRequest;
import com.ojt.project.onlineshop.Entity.User.Role;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public LoginResponse login(LoginRequest loginRequest) throws Exception {
        // Tìm user theo username
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new Exception("Username hoặc password không đúng"));

        // Kiểm tra password
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new Exception("Username hoặc password không đúng");
        }

        // Tạo JWT token
        String token = jwtUtil.generateToken(
                user.getUsername(),
                "ROLE_" + user.getRole().name());

        // Tạo response
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setUsername(user.getUsername());
        response.setRole(user.getRole().name());
        response.setMessage("Đăng nhập thành công");

        return response;
    }

    public LoginResponse register(RegisterRequest request) throws Exception {
        // 1. Kiểm tra username tồn tại [cite: 657]
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new Exception("Username đã tồn tại, vui lòng chọn tên khác!");
        }

        // 2. Tạo User mới
        User newUser = new User();
        newUser.setUsername(request.getUsername());

        // 3. Mã hóa password [cite: 673]
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));

        // 4. Set mặc định Role là USER
        newUser.setRole(User.Role.USER);

        // 5. Lưu xuống DB
        userRepository.save(newUser);

        // 6. Trả về message thành công (FE cần response.data.message)
        return new LoginResponse(null, request.getUsername(), "USER", "Đăng ký thành công! Vui lòng đăng nhập.");
    }
}
