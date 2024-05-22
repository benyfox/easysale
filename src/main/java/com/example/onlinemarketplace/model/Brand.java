package com.example.onlinemarketplace.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
public class Brand extends BaseEntity{
    private String name;
    private String description;
    private String slug;
    @OneToMany(mappedBy = "brand")
    @NotNull
    @JsonIgnore
    private List<Product> products;
}
