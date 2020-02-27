package com.example.demo.controller;


import com.example.demo.model.Order;
import com.example.demo.model.bean.OrderDTO;
import com.example.demo.repository.OrderRepository;
import com.example.demo.service.OrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/pay")
public class PayControllerApi {

    @Resource
    OrderRepository orderRepository;

    @Resource
    OrderService orderService;

    @PostMapping("/createPay")
    public void createOrder(@Valid @RequestBody OrderDTO orderDTO , HttpServletResponse response) throws Exception {
        orderService.pay(orderDTO ,response);
    }

}
