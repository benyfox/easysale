package com.example.onlinemarketplace.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Table(name = "`order`") // fix for reserved keyword "order" in MySQL
public class Order extends BaseEntity {
    @ManyToOne // Remove the @Column annotation

    @JoinColumn(name = "user_id", nullable = true) // nullable is set here
    private User user;

    @Column(nullable = false)
    private String fio;

    @Column(nullable = false)
    private String shippingFirstname;

    @Column(nullable = false)
    private String shippingLastname;

    @Column(nullable = false)
    private String shippingAddress;

    @Column(nullable = false)
    private String shippingOptionalAddress;

    @Column(nullable = false)
    private String shippingCity;

    @Column(nullable = false)
    private String shippingPostal;

    @Column(nullable = false)
    private Long orderNumber;

    private String title;

    @Temporal(TemporalType.TIMESTAMP)
    private Date orderDate;

    @Column(nullable = false)
    private Integer count;

    @Column(nullable = false)
    private Double amount;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "order_product",
            joinColumns = @JoinColumn(name = "order_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "product_id", referencedColumnName = "id"))
    List<Product> products;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;
}
