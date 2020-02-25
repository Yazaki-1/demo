package com.example.demo.service;

import com.example.demo.model.Order;
import com.example.demo.model.QOrder;
import com.example.demo.repository.OrderRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    @Resource
    private OrderRepository orderRepository;

    @Autowired
    JPAQueryFactory jpaQueryFactory;

    /**
     * 创建订单
     * @param order
     */
    public Object createOrder(@RequestBody Order order){
        String uuid = UUID.randomUUID().toString();
        order.setOrderNo(uuid);
        order.setCreateTime(LocalDateTime.now());
        return orderRepository.save(order);
    }

    /**
     * 修改订单
     * @param orderNo
     * @param payment
     * @return
     */
    @Transactional
    public long updateOrder(String orderNo , String payment){
        QOrder order = QOrder.order;
        return jpaQueryFactory
                .update(order)
                .set(order.payment , payment)
                .where(order.orderNo.eq(orderNo))
                .execute();
    }

    /**
     * 查询所有
     * @return
     */
    public List<Order> findAll(){
        QOrder order = QOrder.order;
        return jpaQueryFactory
                .selectFrom(order)
                .orderBy(order.createTime.asc())
                .fetch();
    }

//    public QueryResults<Order> findAllPage(Pageable pageable){
//        QOrder order = QOrder.order;
//        return jpaQueryFactory
//                .selectFrom(order)
//                .orderBy(order.createTime.asc())
//                .offset(pageable.getNumberOfPages())
//                .limit(pageable.getOffset)
//    }

//    /**
//     * 根据生成订单的时间进行查询
//     * @param start
//     * @param end
//     * @return
//     */
//    public List<Order> findByCreateTimeBetween(LocalDateTime start , LocalDateTime end){
//        QOrder order = QOrder.order;
//        return jpaQueryFactory
//                .selectFrom(order)
//                .where(order.createTime.between(start, end))
//                .orderBy(order.createTime.asc())
//                .fetch();
//    }

    /**
     * 根据订单状态查询
     * @param status
     * @return
     */
    public List<Order> findByStatus(String status){
        QOrder order = QOrder.order;
        List<Order> orders = (List<Order>) orderRepository.findAll(
                order.status.eq(status),
                order.createTime.asc()
        );
        return orders;
    }

    /**
     * 根据订单号查找订单
     * @param OrderNo
     * @return
     */
    public Order findByOrderNo(String OrderNo){
        QOrder order = QOrder.order;
        return jpaQueryFactory
                .selectFrom(order)
                .where(order.orderNo.eq(OrderNo))
                .fetchOne();
    }

    /**
     * 根据订单号模糊查找订单
     * @param likeNo
     * @return
     */
    public List<Order> findByOrderNoLike(String likeNo){
        QOrder order = QOrder.order;
        return jpaQueryFactory
                .selectFrom(order)
                .where(order.orderNo.like("%" + likeNo + "%"))
                .fetch();
    }

}
