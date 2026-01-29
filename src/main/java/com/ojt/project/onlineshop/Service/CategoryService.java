package com.ojt.project.onlineshop.Service;


import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import com.ojt.project.onlineshop.Entity.Category;
import com.ojt.project.onlineshop.Repository.CategoryRepository;
import org.springframework.stereotype.Service;





import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy danh mục"));
    }

    public Category create(Category category) {
        if (categoryRepository.existsByName(category.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tên danh mục đã tồn tại");
        }

        return categoryRepository.save(category);
    }

    public Category update(Long id, Category updatedCategory) {
        Category category = findById(id);

        if (categoryRepository.existsByName(updatedCategory.getName())
                && !category.getName().equals(updatedCategory.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tên danh mục đã tồn tại");
        }

        category.setName(updatedCategory.getName());
        return categoryRepository.save(category);
    }


    public void delete(Long id) {
        Category category = findById(id);

        if (category.getProducts() != null && !category.getProducts().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không thể xóa danh mục vì đang có sản phẩm liên kết");
        }

        categoryRepository.deleteById(id);
    }

}
