package com.ojt.project.onlineshop;

import com.ojt.project.onlineshop.Dto.ProductRequest;
import com.ojt.project.onlineshop.Entity.Category;
import com.ojt.project.onlineshop.Entity.Product;
import com.ojt.project.onlineshop.Repository.CategoryRepository;
import com.ojt.project.onlineshop.Repository.ProductRepository;
import com.ojt.project.onlineshop.Service.ProductServiceImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductServiceImpl productService;
    @Test
    void createProduct_Success() {
        // 1. Giả lập dữ liệu đầu vào
        ProductRequest request = new ProductRequest();
        request.setName("Book Java");
        request.setPrice(BigDecimal.valueOf(100));
        request.setCategoryId(1L);
        request.setImageUrl("https://cloudinary.com/img.jpg");

        Category mockCategory = new Category();
        mockCategory.setId(1L);
        mockCategory.setName("IT Books");

        // 2. Mock hành vi Repository
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(mockCategory));
        // Khi save, trả về chính entity đó
        when(productRepository.save(any(Product.class))).thenAnswer(i -> i.getArguments()[0]);

        // 3. Gọi hàm cần test
        Product result = productService.createProduct(request);

        // 4. Kiểm tra kết quả
        assertNotNull(result);
        assertEquals("Book Java", result.getName());
        assertEquals("https://cloudinary.com/img.jpg", result.getImageUrl());
        assertEquals(mockCategory, result.getCategory());

        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void createProduct_CategoryNotFound_ThrowsException() {
        ProductRequest request = new ProductRequest();
        request.setCategoryId(99L);

        when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            productService.createProduct(request);
        });
    }

    // --- TEST UPDATE ---
    @Test
    void updateProduct_Success() {
        // Data cũ
        Long productId = 1L;
        Product existingProduct = new Product();
        existingProduct.setId(productId);
        existingProduct.setName("Old Name");

        // Data mới muốn update
        ProductRequest request = new ProductRequest();
        request.setName("New Name");
        request.setCategoryId(2L);
        // Set thêm các trường bắt buộc khác để tránh NullPointerException khi map
        request.setPrice(BigDecimal.TEN);
        request.setStockQuantity(10);
        request.setAuthor("Author");
        request.setPublisher("Pub");
        request.setBookType("Type");
        request.setNumberOfPages(100);
        request.setPublicationDate(new java.util.Date());


        Category newCategory = new Category();
        newCategory.setId(2L);

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(categoryRepository.findById(2L)).thenReturn(Optional.of(newCategory));
        when(productRepository.save(any(Product.class))).thenAnswer(i -> i.getArguments()[0]);

        Product result = productService.updateProduct(productId, request);

        assertEquals("New Name", result.getName());
        assertEquals(2L, result.getCategory().getId());
    }

    // --- TEST DELETE ---
    @Test
    void deleteProduct_Success() {
        Long id = 1L;
        when(productRepository.existsById(id)).thenReturn(true);

        productService.deleteProduct(id);

        verify(productRepository, times(1)).deleteById(id);
    }
}