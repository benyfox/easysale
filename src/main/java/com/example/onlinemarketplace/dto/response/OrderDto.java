package com.example.onlinemarketplace.dto.response;

import lombok.Data;

import java.util.Date;

@Data
public class OrderDto {
    private Long id;
    private Long orderNumber;
    private String title;
    private Date orderDate;
    private Integer count;
    private Long amount;
    private String status;
}
