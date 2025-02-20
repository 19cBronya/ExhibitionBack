package com.cuit.pay.orders.domain;

import com.cuit.system.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Orders extends BaseEntity {

    /*关联展位id*/
    private Long bid;

    /*关联展览会id*/
    private Long eid;

    /*0 展会订票 1 展位租赁*/
    private String purchaseType;

    /*商品名*/
    private String orderName;

    /*订单编号*/
    private String orderNo;

    /*支付金额*/
    private BigDecimal payMoney;

    /*支付状态 0 未支付 1支付成功 2退款成功*/
    private String payStatus;

    /*票数*/
    private Integer ticketCount;

    /*起始时间*/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String startTime;

    /*结束时间*/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String endTime;

    /*支付编号*/
    private String payNo;

    /*支付时间*/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String payTime;

    /*创建人id*/
    private Long createId;

    /*修改人id*/
    private Long updateId;
}
