package com.example.onlinemarketplace.dto.request;

import com.example.onlinemarketplace.model.OrderStatus;
import com.example.onlinemarketplace.model.Product;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class OrderRequest {
    @JsonProperty("firstName")
    private String shippingFirstname;

    @JsonProperty("lastName")
    private String shippingLastname;

    @JsonProperty("address")
    private String shippingAddress;

    @JsonProperty("apartment")
    private String shippingOptionalAddress;

    @JsonProperty("city")
    private String shippingCity;

    @JsonProperty("postalCode")
    private String shippingPostal;

    private String fio;

    Map<Long, Integer> products;
}
