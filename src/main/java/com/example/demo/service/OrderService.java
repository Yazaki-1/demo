package com.example.demo.service;

import com.alipay.api.AlipayApiException;
import com.example.demo.model.Order;
import com.example.demo.model.bean.OrderDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;



public interface OrderService {

    List<Order> findAll();
    List<Order> findByStatus(String status);
    Order findByOrderNo(String OrderNo);
    List<Order> findByOrderNoLike(String likeNo);
    void pay(OrderDTO orderDTO , HttpServletResponse response) throws Exception;
}
