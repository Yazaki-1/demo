package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Data
@Table(name = "demo_order")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //订单号
    private String orderNo;
    //订单描述
    private String subject;
//    private String userId;
    //支付金额
    private Double ProductPrice;
//    //支付类型
//    private Integer paymentType;
//    //折扣
//    private Integer discount;
    //状态
    private String status;
    //创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private LocalDateTime createTime;
    //支付时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date paymentTime;
//    //对应设备id
//    private String equipmentId;

//    @OneToMany(cascade = CascadeType.MERGE , fetch = FetchType.EAGER , orphanRemoval = true)
//    private OrderItem orderItem;

}
