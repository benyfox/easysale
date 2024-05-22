package com.example.onlinemarketplace.dto.request;

import com.example.onlinemarketplace.model.Product;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class CategoryRequest {
    @NotNull
    private String name;
    @NotNull
    private String slug;
    private String description;
    @NotNull
    private String baseCategory;
    private List<Product> products;
}
