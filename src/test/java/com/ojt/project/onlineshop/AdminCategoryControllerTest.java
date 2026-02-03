package com.ojt.project.onlineshop; // <--- Có thể package của bạn khác, nhớ check

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ojt.project.onlineshop.Controller.AdminCategoryController;
import com.ojt.project.onlineshop.Entity.Category;
import com.ojt.project.onlineshop.Service.CategoryService;
import com.ojt.project.onlineshop.Util.JwtUtil; // <--- NHỚ IMPORT CLASS NÀY
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminCategoryController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AdminCategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CategoryService categoryService;

    // --- SỬA LỖI Ở ĐÂY ---
    // Thêm dòng này để tạo bean giả cho JwtFilter dùng, tránh lỗi thiếu bean
    @MockBean
    private JwtUtil jwtUtil;
    // ---------------------

    @Autowired
    private ObjectMapper objectMapper;

    // --- TEST CREATE ---
    @Test
    void createCategory_Success() throws Exception {
        Category category = new Category();
        category.setName("New Category");
        category.setId(1L);

        when(categoryService.create(any(Category.class))).thenReturn(category);

        mockMvc.perform(post("/api/admin/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(category)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("New Category"));
    }

    // --- TEST UPDATE ---
    @Test
    void updateCategory_Success() throws Exception {
        Long id = 1L;
        Category category = new Category();
        category.setName("Updated Name");

        when(categoryService.update(eq(id), any(Category.class))).thenReturn(category);

        mockMvc.perform(put("/api/admin/categories/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(category)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Name"));
    }

    // --- TEST DELETE ---
    @Test
    void deleteCategory_Success() throws Exception {
        Long id = 1L;
        doNothing().when(categoryService).delete(id);

        mockMvc.perform(delete("/api/admin/categories/{id}", id))
                .andExpect(status().isNoContent());
    }
}