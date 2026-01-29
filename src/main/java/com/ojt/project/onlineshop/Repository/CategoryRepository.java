package com.ojt.project.onlineshop.Repository;


import com.ojt.project.onlineshop.Entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByName(String name);
}
