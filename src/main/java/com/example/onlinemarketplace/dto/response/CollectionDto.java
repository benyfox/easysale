package com.example.onlinemarketplace.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CollectionDto {
    private Long id;
    private String name;
    private String slug;
    private String description;
    private List<ProductDto> products;
}
