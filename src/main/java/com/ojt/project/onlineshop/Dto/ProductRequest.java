package com.ojt.project.onlineshop.Dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {

    // Không cần ID ở đây nếu bạn dùng ID trên URL khi update (PUT /api/products/{id})
    // Hoặc có thể thêm private Long id; nếu muốn dùng chung 1 object.

    @NotBlank(message = "Tên sản phẩm không được để trống")
    private String name;

    private String description;

    @NotNull(message = "Giá sản phẩm không được để trống")
    @Positive(message = "Giá sản phẩm phải lớn hơn 0")
    private BigDecimal price;

    @NotNull(message = "Số lượng trong kho không được để trống")
    @Min(value = 0, message = "Số lượng không được nhỏ hơn 0")
    private Integer stockQuantity;

    // --- ẢNH TỪ CLOUDINARY ---
    // Frontend upload xong sẽ gửi đường link (String) vào đây
    private String imageUrl;

    // --- DANH MỤC (CATEGORY) ---
    // Chỉ cần ID để tìm Category trong DB và gán vào Product
    @NotNull(message = "Vui lòng chọn danh mục sản phẩm (categoryId)")
    private Long categoryId;

    // --- THÔNG TIN CHI TIẾT SÁCH ---
    @NotBlank(message = "Tên tác giả không được để trống")
    private String author;

    @NotBlank(message = "Nhà xuất bản không được để trống")
    private String publisher;

    @NotBlank(message = "Loại bìa/sách không được để trống")
    private String bookType;

    @NotNull(message = "Số trang không được để trống")
    @Min(value = 1, message = "Số trang phải lớn hơn 0")
    private Integer numberOfPages;

    // Định dạng ngày: yyyy-MM-dd
    @NotNull(message = "Ngày xuất bản không được để trống")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Ho_Chi_Minh")
    private Date publicationDate;
}