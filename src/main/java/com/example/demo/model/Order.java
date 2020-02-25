package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "demo_order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //订单号
    private String orderNo;
    private String userId;
    //支付金额
    private String payment;
    //支付类型
    private Integer paymentType;
    //折扣
    private Integer discount;
    //状态
    private String status;
    //创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    //支付时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime paymentTime;
    //更新时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
    //对应设备id
    private String equipmentId;

//    @OneToMany(cascade = CascadeType.MERGE , fetch = FetchType.EAGER , orphanRemoval = true)
//    private OrderItem orderItem;

}
