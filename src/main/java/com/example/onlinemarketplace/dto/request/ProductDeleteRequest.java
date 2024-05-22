package com.example.onlinemarketplace.dto.request;

import com.example.onlinemarketplace.model.Brand;
import com.example.onlinemarketplace.model.Category;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class ProductDeleteRequest {
    private Long id;

    private String name;

    private Float price;

    private List<MultipartFile> imageFileList;

    private MultipartFile thumbnailFile;

    private Category category;

    private String description;

    private Integer quantity;

    private String sku;

    private Map<String, String> features = new HashMap<>();

    private Brand brand;
}
