package com.example.demo.controller;

import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.demo.trade.model.GoodsDetail;
import com.alipay.demo.trade.model.builder.AlipayTradePrecreateRequestBuilder;
import com.alipay.demo.trade.model.builder.AlipayTradeQueryRequestBuilder;
import com.alipay.demo.trade.model.builder.AlipayTradeRefundRequestBuilder;
import com.alipay.demo.trade.model.result.AlipayF2FPrecreateResult;
import com.alipay.demo.trade.model.result.AlipayF2FQueryResult;
import com.alipay.demo.trade.model.result.AlipayF2FRefundResult;
import com.alipay.demo.trade.service.AlipayTradeService;
import com.example.demo.properties.AliPayProperties;
import com.example.demo.util.PayUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/alipay")
public class AliPayController {

    @Autowired
    private AlipayTradeService alipayTradeService;

    @Autowired
    private AliPayProperties aliPayProperties;

    @PostMapping("/precreate")
    public void precreate(HttpServletRequest request , HttpServletResponse response) throws Exception{
        //模拟订单 全部写死 要修改
        //订单号
        String outTradeNo = UUID.randomUUID().toString();
        //订单标题
        String subject = "test";
        //订单描述
        String body = "购买一件一共15.55元";
        // (必填) 订单总金额，单位为元，不能超过1亿元
        String totalAmount = "23.33";
        // 卖家支付宝账号ID，用于支持一个签约账号下支持打款到不同的收款账号，(打款到sellerId对应的支付宝账号)
        // 如果该字段为空，则默认为与支付宝签约的商户的PID，也就是appid对应的PID
        String sellerId = "";
        // (必填) 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持
        String storeId = "test_store_id";
        // 商户操作员编号，添加此参数可以为商户操作员做销售统计
        String operatorId = "test_operator_id";

        //写死的商品
        List<GoodsDetail> goodsDetails = new ArrayList<>();
        GoodsDetail goods1 = GoodsDetail.newInstance("goods_id001", "全麦小面包", 1, 1);
        goodsDetails.add(goods1);
        GoodsDetail goods2 = GoodsDetail.newInstance("goods_id002", "黑人牙刷", 1, 2);
        goodsDetails.add(goods2);

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
        String qrCodeUrl = null;
        switch (result.getTradeStatus()) {
            case SUCCESS:
                log.info("支付宝预下单成功: )");

                AlipayTradePrecreateResponse res = result.getResponse();
                BufferedImage image = PayUtil.getQRCodeImge(res.getQrCode());

                response.setContentType("image/jpeg");
                response.setHeader("Pragma","no-cache");
                response.setHeader("Cache-Control","no-cache");
                response.setIntHeader("Expires",-1);
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
                break;
        }
    }

    @GetMapping("/query")
    public String query(String orderNo){

        AlipayTradeQueryRequestBuilder builder = new AlipayTradeQueryRequestBuilder()
                .setOutTradeNo(orderNo);
        AlipayF2FQueryResult result = alipayTradeService.queryTradeResult(builder);
        switch (result.getTradeStatus()) {
            case SUCCESS:
                log.info("查询返回该订单支付成功: )");

                AlipayTradeQueryResponse resp = result.getResponse();
                log.info(resp.getTradeStatus());
//                log.info(resp.getFundBillList());
                break;

            case FAILED:
                log.error("查询返回该订单支付失败!!!");
                break;

            case UNKNOWN:
                log.error("系统异常，订单支付状态未知!!!");
                break;

            default:
                log.error("不支持的交易状态，交易返回异常!!!");
                break;
        }
        return result.getResponse().getBody();
    }

    /**
     * 退款
     * @param orderNo 商户订单号
     * @return
     */
    @PostMapping("/refund")
    public String refund(String orderNo){
        AlipayTradeRefundRequestBuilder builder = new AlipayTradeRefundRequestBuilder()
                .setOutTradeNo(orderNo)
                .setRefundAmount("0.01")
                .setRefundReason("当面付退款测试")
                .setOutRequestNo(String.valueOf(System.nanoTime()))
                .setStoreId("A1");
        AlipayF2FRefundResult result = alipayTradeService.tradeRefund(builder);
        switch (result.getTradeStatus()) {
            case SUCCESS:
                log.info("支付宝退款成功: )");
                break;

            case FAILED:
                log.error("支付宝退款失败!!!");
                break;

            case UNKNOWN:
                log.error("系统异常，订单退款状态未知!!!");
                break;

            default:
                log.error("不支持的交易状态，交易返回异常!!!");
                break;
        }

        return result.getResponse().getBody();
    }


    /**
     * 扫码付异步结果通知
     * https://docs.open.alipay.com/194/103296
     * @param request
     */
    @RequestMapping("/notify")
    public String notify(HttpServletRequest request) throws AlipayApiException {
        // 验签，防止黑客篡改参数
        Map<String, String[]> parameterMap = request.getParameterMap();
        parameterMap.forEach((key, value) -> System.out.println(key+"="+value[0]));

        // https://docs.open.alipay.com/54/106370
        // 获取支付宝POST过来反馈信息
        Map<String,String> params = new HashMap<>();
        Map requestParams = request.getParameterMap();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            params.put(name, valueStr);
        }

        boolean flag = AlipaySignature.rsaCheckV1(params,
                aliPayProperties.getAliPayPublicKey(),
                aliPayProperties.getCharset(),
                aliPayProperties.getSignType());

        if (flag) {
            /**
             * TODO 需要严格按照如下描述校验通知数据的正确性
             *
             * 商户需要验证该通知数据中的out_trade_no是否为商户系统中创建的订单号，
             * 并判断total_amount是否确实为该订单的实际金额（即商户订单创建时的金额），
             * 同时需要校验通知中的seller_id（或者seller_email) 是否为out_trade_no这笔单据的对应的操作方（有的时候，一个商户可能有多个seller_id/seller_email），
             *
             * 上述有任何一个验证不通过，则表明本次通知是异常通知，务必忽略。
             * 在上述验证通过后商户必须根据支付宝不同类型的业务通知，正确的进行不同的业务处理，并且过滤重复的通知结果数据。
             * 在支付宝的业务通知中，只有交易通知状态为TRADE_SUCCESS或TRADE_FINISHED时，支付宝才会认定为买家付款成功。
             */

            return "success";
        }

        return null;
    }

}
