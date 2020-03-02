package com.example.demo.controller;

import com.example.demo.model.SystemCoupon;
import com.example.demo.model.bean.SysCouponDTO;
import com.example.demo.service.SysCouponService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/coupon")
public class SysCouponController {

    @Resource
    SysCouponService sysCouponService;

    @PostMapping("/create")
    public Object create(@Valid @RequestBody SysCouponDTO para){
        return sysCouponService.creatCoupon(para);
    }

    @GetMapping("/allCoupon")
    public List<SystemCoupon> allCoupon(){
        return sysCouponService.findAll();
    }

    @GetMapping("/findStatus")
    public List<SystemCoupon> findByStatus(String status){
        return sysCouponService.findByStatus(status);
    }



}
