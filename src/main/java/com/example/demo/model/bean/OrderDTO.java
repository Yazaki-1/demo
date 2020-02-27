package com.example.demo.model.bean;

import lombok.Data;

@Data
public class OrderDTO {
    private String orderNo;
    private String status;
    private String productId;
    private String ProductName;
    private String subject;
    private String productPrice;
}
