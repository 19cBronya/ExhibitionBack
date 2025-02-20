package com.cuit.pay.aliPay.domain;

import lombok.Data;

@Data
public class AliPay {
    // 订单编号
    private String traceNo;
    // 订单的总金额
    private double totalAmount;
    //支付对象信息
    private String subject;

    private String alipayTraceNo;
}

