package com.example.onlinemarketplace.dto.request;

import com.example.onlinemarketplace.model.Product;
import lombok.Data;

import java.util.List;

@Data
public class CollectionRequest {
    private Long id;
    private String name;
    private String description;
    private List<Long> products;
}
