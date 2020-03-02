package com.example.demo.service.impl;

import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.demo.trade.model.GoodsDetail;
import com.alipay.demo.trade.model.builder.AlipayTradePrecreateRequestBuilder;
import com.alipay.demo.trade.model.result.AlipayF2FPrecreateResult;
import com.alipay.demo.trade.service.AlipayTradeService;
import com.example.demo.model.Order;
import com.example.demo.model.QOrder;
import com.example.demo.model.bean.OrderDTO;
import com.example.demo.properties.AliPayProperties;
import com.example.demo.repository.OrderRepository;
import com.example.demo.service.OrderService;
import com.example.demo.util.PayUtil;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.function.ServerResponse;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Resource
    private OrderRepository orderRepository;
    @Autowired
    JPAQueryFactory jpaQueryFactory;
    @Autowired
    private AlipayTradeService alipayTradeService;
    @Autowired
    private AliPayProperties aliPayProperties;

//    /**
//     * 修改订单
//     * @param orderNo
//     * @param status
//     * @return
//     */
//    @Transactional
//    @Modifying
//    public long updateOrder(String orderNo, String status) {
//        QOrder order = QOrder.order;
//        return jpaQueryFactory
//                .update(order)
//                .set(order.status , status)
//                .where(order.orderNo.eq(orderNo))
//                .execute();
//    }

    /**
     * 查询所有
     *
     * @return
     */
    public List<Order> findAll() {
        QOrder order = QOrder.order;
        return jpaQueryFactory
                .selectFrom(order)
                .orderBy(order.createTime.asc())
                .fetch();
    }

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
     *
     * @param status
     * @return
     */
    public List<Order> findByStatus(String status) {
        QOrder order = QOrder.order;
        List<Order> orders = (List<Order>) orderRepository.findAll(
                order.status.eq(status),
                order.createTime.asc()
        );
        return orders;
    }

    /**
     * 根据订单号查找订单
     *
     * @param OrderNo
     * @return
     */
    public Order findByOrderNo(String OrderNo) {
        QOrder order = QOrder.order;
        return jpaQueryFactory
                .selectFrom(order)
                .where(order.orderNo.eq(OrderNo))
                .fetchOne();
    }

    /**
     * 根据订单号模糊查找订单
     *
     * @param likeNo
     * @return
     */
    public List<Order> findByOrderNoLike(String likeNo) {
        QOrder order = QOrder.order;
        return jpaQueryFactory
                .selectFrom(order)
                .where(order.orderNo.like("%" + likeNo + "%"))
                .fetch();
    }

    public void pay(@Valid @RequestBody OrderDTO orderDTO , HttpServletResponse response) throws Exception{

        Order order = new Order();
        //订单号
        String outTradeNo = orderDTO.getOrderNo();
        //订单标题
        String subject = orderDTO.getSubject();
        // (必填) 订单总金额，单位为元，不能超过1亿元
        String totalAmount = orderDTO.getProductPrice().toString();
        //订单描述
        String body = new StringBuilder().append("订单").append(orderDTO.getOrderNo()).append("一共").append(totalAmount).append("元").toString();
        // 卖家支付宝账号ID，用于支持一个签约账号下支持打款到不同的收款账号，(打款到sellerId对应的支付宝账号)
        // 如果该字段为空，则默认为与支付宝签约的商户的PID，也就是appid对应的PID
        String sellerId = "";
        // (必填) 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持
        String storeId = "test_store_id";
        // 商户操作员编号，添加此参数可以为商户操作员做销售统计
        String operatorId = "test_operator_id";

        //商品List
        List<GoodsDetail> goodsDetails = new ArrayList<>();
        GoodsDetail goods = GoodsDetail.newInstance(orderDTO.getProductId(), orderDTO.getProductName(), orderDTO.getProductPrice().longValue(), 1);
        goodsDetails.add(goods);
        //支付超时时间定义为5min
        String timeoutExpress = "5m";
        AlipayTradePrecreateRequestBuilder builder =new AlipayTradePrecreateRequestBuilder()
                .setSubject(subject)
                .setTotalAmount(totalAmount)
                .setOutTradeNo(outTradeNo)
                .setSellerId(sellerId)
                .setBody(body)
                .setOperatorId(operatorId)
                .setStoreId(storeId)
                .setTimeoutExpress(timeoutExpress)
                //支付宝服务器主动通知商户服务器里指定的页面http路径,根据需要设置
                .setNotifyUrl(aliPayProperties.getNotifyUrl())
                .setGoodsDetailList(goodsDetails);

        AlipayF2FPrecreateResult result = alipayTradeService.tradePrecreate(builder);

        switch (result.getTradeStatus()) {
            case SUCCESS:
                log.info("支付宝预下单成功: )");

                order.setStatus("未支付");
                order.setCreateTime(LocalDateTime.now());
                BeanUtils.copyProperties(orderDTO , order);
                orderRepository.save(order);

                AlipayTradePrecreateResponse res = result.getResponse();
                BufferedImage image = PayUtil.getQRCodeImge(res.getQrCode());

                response.setContentType("image/jpeg");
                response.setHeader("Pragma", "no-cache");
                response.setHeader("Cache-Control", "no-ca che");
                response.setIntHeader("Expires", -1);
                ImageIO.write(image, "JPEG", response.getOutputStream());
                break;
            case FAILED:
                log.error("支付宝预下单失败!!!");
                break;
            case UNKNOWN:
                log.error("系统异常，预下单状态未知!!!");
                break;

            default:
                log.error("不支持的交易状态，交易返回异常!!!");
        }
    }


    public String AliCallback(Map<String , String> params){
        String orderNo = params.get("out_trade_no");
        String tradeStatus = params.get("trade_status");
        Order order = findByOrderNo(orderNo);
        if (orderNo == null) {
            return ServerResponse.badRequest().body("非正确订单，回调忽略").toString();
        }
        if(AliPayCallback.TRADE_STATUS_TRADE_SUCCESS.equals(tradeStatus)){
            order.setPaymentTime(strToDate(params.get("gmt_payment")));
            order.setStatus("已支付");
        }else{
            order.setStatus("支付失败");
        }
        orderRepository.save(order);
        return "ok";
    }

    public interface AliPayCallback{
        String TRADE_STATUS_WAIT_BUYER_PAY = "WAIT_BUYER_PAY";
        String TRADE_STATUS_TRADE_SUCCESS = "TRADE_SUCCESS";
    }

    public static final String STANDARD_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static Date strToDate(String dateTimeStr){
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(STANDARD_FORMAT);
        DateTime dateTime = dateTimeFormatter.parseDateTime(dateTimeStr);
        return dateTime.toDate();
    }

}
