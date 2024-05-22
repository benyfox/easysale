package com.example.onlinemarketplace.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor(force = true)
@Entity
@Table
public class ProductImage extends Image {
    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    @NotNull
    @JsonIgnore
    private final Product product;
}
