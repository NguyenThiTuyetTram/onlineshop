package com.ojt.project.onlineshop.Service;

import com.ojt.project.onlineshop.Dto.ProductRequest;
import com.ojt.project.onlineshop.Entity.Category;
import com.ojt.project.onlineshop.Entity.Product;
import com.ojt.project.onlineshop.Repository.CategoryRepository;
import com.ojt.project.onlineshop.Repository.ProductRepository;
import com.ojt.project.onlineshop.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    // ===== ADMIN (CREATE / UPDATE / DELETE) =====

    @Override
    public Product createProduct(ProductRequest request) {
        // 1. Kiểm tra xem Category có tồn tại không
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Danh mục không tồn tại với ID: " + request.getCategoryId()));

        // 2. Khởi tạo Product mới
        Product product = new Product();

        // 3. Gọi hàm map dữ liệu từ Request sang Entity
        mapRequestToEntity(request, product);

        // 4. Gán Category và lưu xuống DB
        product.setCategory(category);
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Long id, ProductRequest request) {
        // 1. Tìm sản phẩm cũ cần sửa
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sản phẩm với ID: " + id));

        // 2. Kiểm tra Category mới (nếu người dùng đổi danh mục)
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Danh mục không tồn tại với ID: " + request.getCategoryId()));

        // 3. Cập nhật dữ liệu mới vào sản phẩm cũ
        mapRequestToEntity(request, existingProduct);

        // 4. Gán Category mới và lưu lại
        existingProduct.setCategory(category);
        return productRepository.save(existingProduct);
    }

    @Override
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Không tìm thấy sản phẩm để xóa");
        }
        productRepository.deleteById(id);
    }

    // ===== PUBLIC / READ-ONLY =====

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại với ID: " + id));
    }

    @Override
    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    @Override
    public Page<Product> searchByName(String keyword, Pageable pageable) {
        return productRepository.findByNameContainingIgnoreCase(keyword, pageable);
    }

    @Override
    public Page<Product> findByCategoryId(Long categoryId, Pageable pageable) {
        // Kiểm tra category có tồn tại không trước khi tìm (Optional - tuỳ logic của bạn)
        if (!categoryRepository.existsById(categoryId)) {
            throw new RuntimeException("Danh mục không tồn tại");
        }
        return productRepository.findByCategory_Id(categoryId, pageable);
    }

    // ===== HÀM PHỤ (HELPER METHOD) =====
    // Tách phần mapping ra để dùng chung cho cả Create và Update cho gọn code
    private void mapRequestToEntity(ProductRequest request, Product product) {
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStockQuantity(request.getStockQuantity());

        // Quan trọng: Lưu link ảnh từ Cloudinary
        product.setImageUrl(request.getImageUrl());

        product.setAuthor(request.getAuthor());
        product.setPublisher(request.getPublisher());
        product.setBookType(request.getBookType());
        product.setNumberOfPages(request.getNumberOfPages());
        product.setPublicationDate(request.getPublicationDate());
    }
}