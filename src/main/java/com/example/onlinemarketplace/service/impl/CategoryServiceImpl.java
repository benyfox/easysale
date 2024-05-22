package com.example.onlinemarketplace.service.impl;

import com.example.onlinemarketplace.dto.request.CategoryRequest;
import com.example.onlinemarketplace.dto.response.CategoryDto;
import com.example.onlinemarketplace.exception.CategoryNotFoundException;
import com.example.onlinemarketplace.exception.CollectionNotFoundException;
import com.example.onlinemarketplace.model.Category;
import com.example.onlinemarketplace.model.CategoryType;
import com.example.onlinemarketplace.model.Collection;
import com.example.onlinemarketplace.model.Product;
import com.example.onlinemarketplace.repository.CategoryRepository;
import com.example.onlinemarketplace.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<CategoryDto> getAllCategories() {
        return (categoryRepository
                .findAll())
                .stream()
                .map(this::convertToCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public Category createNewCategory(Category category) {
        Objects.requireNonNull(category, "category can not be null");
        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(Category category) {
        Objects.requireNonNull(category, "category can not be null");
        return categoryRepository.save(category);
    }

    @Override
    public Boolean deleteCategory(Category category) {
        Objects.requireNonNull(category, "category can not be null");
        Long id = category.getId();
        categoryRepository.delete(category);
        return !categoryRepository.existsById(id);
    }

    @Override
    public boolean existsById(Long id) {
        Objects.requireNonNull(id, "id can not be null");
        return categoryRepository.existsById(id);
    }

    @Override
    public List<CategoryDto> getCategoryByBaseCategory(CategoryType categoryType) {
        Objects.requireNonNull(categoryType, "category type can not be null");
        return (categoryRepository
                .getCategoryByBaseCategory(categoryType))
                .stream()
                .map(this::convertToCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategoryBySlug(String name) {
        Objects.requireNonNull(name, "name can not be null");
        return convertToCategoryDto(categoryRepository
                .getCategoryBySlug(name)
                .orElseThrow(CategoryNotFoundException::new));
    }

    @Override
    public List<CategoryDto> getCategoryByNameLike(String name) {
        Objects.requireNonNull(name, "name can not be null");
        return (categoryRepository
                .getCategoryByNameLike(name))
                .stream()
                .map(this::convertToCategoryDto)
                .collect(Collectors.toList());
    }

    private CategoryDto convertToCategoryDto(Category category) {
        return modelMapper.map(category, CategoryDto.class);
    }
    private Category convertToCategory(CategoryRequest categoryRequest) { return modelMapper.map(categoryRequest, Category.class); }
}
