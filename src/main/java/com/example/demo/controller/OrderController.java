package com.example.demo.controller;

import com.example.demo.model.Order;
import com.example.demo.service.OrderService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Resource
    OrderService orderService;


    @GetMapping("/findAll")
    public List<Order> findAllOrders(){
        return orderService.findAll();
    }

    @GetMapping("/findStatus")
    public List<Order> findOrderStatus(@RequestParam(value = "status") String status){
        return orderService.findByStatus(status);
    }

    @GetMapping("/findNo")
    public Order findOrderByNo(@RequestParam(value = "orderNo") String orderNo){
        return orderService.findByOrderNo(orderNo);
    }

//    /**
//     * 修改订单
//     * @param orderNo
//     * @param payment
//     * @return
//     */
//    @PutMapping("/updateOrder")
//    public long updateOrder(@RequestParam(value = "orderNo") String orderNo , @RequestParam(value = "payment") String payment){
//        return orderService.updateOrder(orderNo , payment);
//    }

    /**
     * 订单号模糊搜索
     * @param likeNo
     * @return
     */
    @GetMapping("/findLike")
    public List<Order> findOrderNoLike(@RequestParam(value = "likeNo") String likeNo){
        return orderService.findByOrderNoLike(likeNo);
    }



}
