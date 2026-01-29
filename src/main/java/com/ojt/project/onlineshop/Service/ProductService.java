package com.ojt.project.onlineshop.Service;

import com.ojt.project.onlineshop.Dto.ProductRequest; // Nhớ import cái này
import com.ojt.project.onlineshop.Entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    // ===== ADMIN (CREATE / UPDATE / DELETE) =====

    // Thay đổi: Nhận vào ProductRequest (DTO) thay vì Entity
    Product createProduct(ProductRequest request);

    // Thay đổi: Nhận vào ProductRequest (DTO)
    Product updateProduct(Long id, ProductRequest request);

    void deleteProduct(Long id);

    // ===== PUBLIC / READ-ONLY =====

    Product getProductById(Long id);

    Page<Product> getAllProducts(Pageable pageable);

    Page<Product> searchByName(String keyword, Pageable pageable);

    Page<Product> findByCategoryId(Long categoryId, Pageable pageable);
}