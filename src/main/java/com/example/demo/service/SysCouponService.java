package com.example.demo.service;

import com.example.demo.model.SystemCoupon;
import com.example.demo.model.bean.SysCouponDTO;

import java.util.List;

public interface SysCouponService {

    Object creatCoupon(SysCouponDTO para);
    List<SystemCoupon> findAll();
    List<SystemCoupon> findByStatus(String status);
}
