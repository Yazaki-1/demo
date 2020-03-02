package com.example.demo.controller;

import com.example.demo.model.Order;
import com.example.demo.model.bean.OrderDTO;
import com.example.demo.repository.OrderRepository;
import com.example.demo.service.OrderService;
import com.example.demo.util.LayuiRequest;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Resource
    OrderService orderService;

    @Resource
    OrderRepository orderRepository;

    @PostMapping("/createPay")
    public void createOrder(@Valid @RequestBody OrderDTO orderDTO , HttpServletResponse response) throws Exception {
        orderService.pay(orderDTO ,response);
    }


    /**
     * 查找所有
     * @return
     */
    @GetMapping("/findAll")
    public List<Order> findAllOrders(){
        return orderService.findAll();
    }

    /**
     * 根据状态查询
     * @param status
     * @return
     */
    @GetMapping("/findStatus")
    public List<Order> findOrderStatus(@RequestParam(value = "status") String status){
        return orderService.findByStatus(status);
    }

    /**
     * 根据订单号查订单信息
     * @param orderNo
     * @return
     */
    @GetMapping("/findNo")
    public Order findOrderByNo(@RequestParam(value = "orderNo") String orderNo){
        return orderService.findByOrderNo(orderNo);
    }
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
