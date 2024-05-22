package com.example.onlinemarketplace.repository;

import com.example.onlinemarketplace.model.Category;
import com.example.onlinemarketplace.model.CategoryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    boolean existsById(Long id);

    List<Category> getCategoryByBaseCategory(CategoryType categoryType);

    List<Category> getCategoryByNameLike(String category);

    Optional<Category> getCategoryBySlug(String name);
}
