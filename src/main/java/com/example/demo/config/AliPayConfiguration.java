package com.example.demo.config;

import com.alipay.demo.trade.service.AlipayTradeService;
import com.alipay.demo.trade.service.impl.AlipayTradeServiceImpl;
import com.example.demo.properties.AliPayProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(AliPayProperties.class)
public class AliPayConfiguration {

    private AliPayProperties aliPayProperties;

    public AliPayConfiguration(AliPayProperties aliPayProperties){
        this.aliPayProperties = aliPayProperties;
    }

    @Bean
    public AlipayTradeService alipayTradeService(){
        return new AlipayTradeServiceImpl.ClientBuilder()
                .setGatewayUrl(aliPayProperties.getGatewayUrl())
                .setAppid(aliPayProperties.getAppid())
                .setPrivateKey(aliPayProperties.getAppPrivateKey())
                .setAlipayPublicKey(aliPayProperties.getAliPayPublicKey())
                .setSignType(aliPayProperties.getSignType())
                .build();
    }

}