package com.example.onlinemarketplace.dto.request;

import com.example.onlinemarketplace.model.Brand;
import com.example.onlinemarketplace.model.Category;
import com.example.onlinemarketplace.model.Seller;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class ProductRequest {

    @NotNull
    private String name;

    @NotNull
    private Float price;

    @NotNull
    private List<MultipartFile> imageFileList;

    @NotNull
    private MultipartFile thumbnailFile;

    @NotNull
    private Category category;

    private String description;

    private Integer quantity;

    private String sku;

    private Map<String, String> features = new HashMap<>();

    private Brand brand;
}
