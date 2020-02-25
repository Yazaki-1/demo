package com.example.demo.properties;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

import javax.annotation.PostConstruct;

@Data
@Slf4j
@ConfigurationProperties(prefix = "pay.alipay")
public class AliPayProperties {

    private String gatewayUrl;

    private String appid;

    /**
     * RSA私钥
     */
    private String appPrivateKey;

    /**
     * RSA公钥
     */
    private String aliPayPublicKey;

    private String signType = "RSA2";

    private String formate = "json";

    private String charset = "UTF-8";

    /**
     * 同步地址
     */
    private String returnUrl;

    /**
     * 异步地址
     */
    private String notifyUrl;

    /**
     * 最大查询次数
     */
    private static int maxQueryRetry = 5;
    /**
     * 查询间隔（毫秒）
     */
    private static long queryDuration = 5000;
    /**
     * 最大撤销次数
     */
    private static int maxCancelRetry = 3;
    /**
     * 撤销间隔（毫秒）
     */
    private static long cancelDuration = 3000;

    private AliPayProperties() {
    }

    @PostConstruct
    public void ini(){
        log.info(description());
    }

    public String description(){
        StringBuilder stringBuilder = new StringBuilder("\nConfigs{");
        stringBuilder.append("支付宝网关").append(gatewayUrl).append("\n");
        stringBuilder.append(",appid").append(appid).append("\n");
        stringBuilder.append(",商户RSA私钥").append(getKeyDescription(appPrivateKey)).append("\n");
        stringBuilder.append(",支付宝RSA公钥").append(getKeyDescription(aliPayPublicKey)).append("\n");
        stringBuilder.append(",签名类型：").append(signType).append("\n");

        stringBuilder.append(",查询重试次数").append(maxQueryRetry).append("\n");
        stringBuilder.append(",查询间隔").append(queryDuration).append("\n");
        stringBuilder.append(",撤销尝试次数").append(maxCancelRetry).append("\n");
        stringBuilder.append(",撤销重试间隔").append(cancelDuration).append("\n");
        stringBuilder.append("}");

        return stringBuilder.toString();
    }

    public String getKeyDescription(String key){
        int showLength = 6;
        if(StringUtils.isNotEmpty(key) && key.length() > showLength) {
            return new StringBuilder(key.substring(0 , showLength)).append("******").append(key.substring(key.length() - showLength)).toString();
        }
        return null;
    }

}