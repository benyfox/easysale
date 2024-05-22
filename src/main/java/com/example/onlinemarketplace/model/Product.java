package com.example.onlinemarketplace.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table
//@Table(uniqueConstraints = {
//        @UniqueConstraint(columnNames = {"name", "seller_id"})
//})
public class Product extends BaseEntity {
    @NotNull
    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Seller seller;

    @NotNull
    private String name;

    @NotNull
    @Min(value = 1)
    private Float price;

    @Min(value = 1)
    private Float salePrice;

    private boolean isSale = false;

    private boolean isActive = true;

    private Float shippingCost;

    private Integer quantity;

    private String description;

    @ManyToOne
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Category category;

    @ElementCollection
    @MapKeyColumn(name="desc_property")
    @Column(name="desc_value")
    @CollectionTable(name="product_features", joinColumns=@JoinColumn(name="id"))
    private Map<String, String> features = new HashMap<>();

    @Column(unique = true)
    private String sku;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private Brand brand;

    @ManyToMany
    @JoinTable(name = "collection_product",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "collection_id"))
    private List<Collection> collections;

    @Builder.Default
    private Boolean isAvailable = true;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private ProductThumbnail thumbnail;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImage> imageList = new ArrayList<>();

    @Range(min = 0, max = 95)
    @Builder.Default
    private Integer discountRate = 0;

    @Range(min = 1, max = 5)
    @Builder.Default
    @Column(columnDefinition = "float default 5.0")
    private float rating = 5;

    public void addImage(ProductImage productImage) {
        imageList.add(productImage);
    }
}
