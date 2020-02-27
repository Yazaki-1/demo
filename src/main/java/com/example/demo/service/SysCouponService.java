package com.example.demo.service;

import com.example.demo.model.SystemCoupon;
import com.example.demo.model.bean.SysCouponInfo;

import java.util.List;

public interface SysCouponService {

    Object creatCoupon(SysCouponInfo para);
    List<SystemCoupon> findAll();
    List<SystemCoupon> findByStatus(String status);
}
