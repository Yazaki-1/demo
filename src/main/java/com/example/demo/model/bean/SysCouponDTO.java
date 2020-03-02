package com.example.demo.model.bean;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class SysCouponDTO {

    //券name
    @NotBlank
    @NotNull
    private String couponName;
    //券面值
    @NotBlank
    @NotNull
    private String couponParValue;
    //最低消费（满减
    @NotBlank
    @NotNull
    private String minimumConsumption;
    //有效期
    @NotBlank
    @NotNull
    private String validityPeriod;

}
