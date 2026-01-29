package com.ojt.project.onlineshop.Controller;

import com.ojt.project.onlineshop.Entity.Product;
import com.ojt.project.onlineshop.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products") // Hoặc /api/public/products tuỳ cấu hình của bạn
public class PublicProductController {

    @Autowired
    private ProductService productService;

    // 1. Lấy tất cả sản phẩm (Phân trang)
    @GetMapping
    public ResponseEntity<Page<Product>> getAllProducts(@PageableDefault(size = 10) Pageable pageable) {
        // SỬA LỖI Ở ĐÂY: Đổi findAll -> getAllProducts
        Page<Product> products = productService.getAllProducts(pageable);
        return ResponseEntity.ok(products);
    }

    // 2. Tìm kiếm sản phẩm theo tên
    @GetMapping("/search")
    public ResponseEntity<Page<Product>> searchProducts(@RequestParam String keyword,
                                                        @PageableDefault(size = 10) Pageable pageable) {
        Page<Product> products = productService.searchByName(keyword, pageable);
        return ResponseEntity.ok(products);
    }

    // 3. Lấy sản phẩm theo danh mục
    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Page<Product>> getProductsByCategory(@PathVariable Long categoryId,
                                                               @PageableDefault(size = 10) Pageable pageable) {
        Page<Product> products = productService.findByCategoryId(categoryId, pageable);
        return ResponseEntity.ok(products);
    }

    // 4. Xem chi tiết 1 sản phẩm
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductDetail(@PathVariable Long id) {
        try {
            // Lưu ý: Trong Service mình đặt là getProductById
            Product product = productService.getProductById(id);
            return ResponseEntity.ok(product);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}