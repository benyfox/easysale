package com.example.onlinemarketplace.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table
public class ProductThumbnail extends Image {
    @OneToOne(orphanRemoval = true, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    @NotNull
    @JsonIgnore
    private Product product;
}
