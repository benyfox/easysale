package com.example.onlinemarketplace.dto.response;

import com.example.onlinemarketplace.model.Product;
import lombok.Data;

import java.util.List;

@Data
public class CategoryDto {
    private String name;
    private String slug;
    private String baseCategory;
}
