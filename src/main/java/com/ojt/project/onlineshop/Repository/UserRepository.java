package com.ojt.project.onlineshop.Repository;

import com.ojt.project.onlineshop.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // Tìm user theo username
    // Spring tự động tạo query dựa trên tên method
    Optional<User> findByUsername(String username);
    
    // Kiểm tra username đã tồn tại chưa
    boolean existsByUsername(String username);
}
