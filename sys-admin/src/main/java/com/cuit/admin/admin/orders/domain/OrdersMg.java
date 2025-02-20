package com.cuit.admin.admin.orders.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrdersMg {
    @ExcelProperty(value = "订单类型")
    private String purchaseType;
    @ExcelProperty(value = "订单编号")
    private String orderNo;
    @ExcelProperty(value = "订单名")
    private String orderName;
    @ExcelProperty(value = "支付金额")
    private BigDecimal payMoney;
    @ExcelProperty(value = "支付状态")
    private String payStatus;
    @ExcelProperty(value = "订单创建时间")
    private String createTime;
    @ExcelProperty(value = "订单创建人")
    private String createName;
    @ExcelProperty(value = "订单支付流水号")
    private String payNo;
    @ExcelProperty(value = "订单支付时间")
    private String payTime;
}
