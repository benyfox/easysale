package com.example.onlinemarketplace.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class BrandsProductsDto {
    private Long id;
    private String name;
    private String slug;
    private List<ProductDto> products;
}
