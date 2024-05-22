package com.example.onlinemarketplace.dto.response;

import com.example.onlinemarketplace.model.Brand;
import com.example.onlinemarketplace.model.Category;
import com.example.onlinemarketplace.model.Image;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class ProductDto {

    private Long id;

    @JsonProperty("title")
    private String name;

    private Float price;

    @JsonProperty("sale_price")
    private Float salePrice;

    private Float shippingCost;

    private String description;

    private Boolean isAvailable = true;

    @JsonProperty("vendor")
    private String sellerName;

    private Category category;

    private Brand brand;

    private String baseCategory;

    private Integer discountRate;

    private Float rating;

    private Map<String, String> features;

    private String sku;

    @JsonProperty("images")
    private List<Image> imageList;

    private Image thumbnail;

    private Date createdAt;
}
