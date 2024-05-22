package com.example.onlinemarketplace.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table
public class Collection extends BaseEntity {
    private String name;
    private String slug;
    private String description;

    @ManyToMany(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    @JoinTable(name = "collection_product",
            joinColumns = @JoinColumn(name = "collection_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id"))
    private List<Product> products;

    public void addProduct(Product product) {
        this.products.add(product);
    }

    public void addProducts(List<Product> products) {
        this.products.addAll(products);
    }
}
