package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "sys_coupon")
public class SystemCoupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //随机生成
    private String couponId;
    //券name
    private String couponName;
    //券面值
    private String couponParValue;
    //最低消费（满减
    private String minimumConsumption;
    //有效期
    private String validityPeriod;
    //领取开始时间
    private LocalDateTime startTime;
    //领取结束时间
    private LocalDateTime endTime;
    //数量
    private String amount;
    //生成时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime creationTime;
    //状态
    private String status;
}
