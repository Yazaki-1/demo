package com.example.demo.controller;

import com.example.demo.model.para.SysCouponParaOne;
import com.example.demo.service.SysCouponService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/coupon")
public class SysCouponController {

    @Resource
    SysCouponService sysCouponService;

    @PostMapping
    Object create(@Valid @RequestBody SysCouponParaOne para){
        return sysCouponService.creatCoupon(para);
    }



}
