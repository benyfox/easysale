package com.example.onlinemarketplace.controller;

import com.example.onlinemarketplace.dto.response.CategoryDto;
import com.example.onlinemarketplace.model.Category;
import com.example.onlinemarketplace.model.CategoryType;
import com.example.onlinemarketplace.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/category")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/all")
    public List<CategoryDto> get() {
        return categoryService.getAllCategories();
    }

    @GetMapping
    public List<CategoryDto> getByCategoryType(@RequestParam(required = false) CategoryType type) {
        if (type == null)
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "query can not be null");

        return categoryService.getCategoryByBaseCategory(type);
    }

    @GetMapping("/{slug}")
    public CategoryDto getBySlug(@PathVariable String slug) {
        if (slug == null)
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "query can not be null");

        return categoryService.getCategoryBySlug(slug);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping
    public Category create(@Valid @RequestBody Category category) {
        return categoryService.createNewCategory(category);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping
    public boolean delete(@Valid @RequestBody Category category) {
        return categoryService.deleteCategory(category);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping
    public Category update(@Valid @RequestBody Category category) {
        return categoryService.updateCategory(category);
    }
}
