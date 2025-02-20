package com.cuit.pay.orders.domain.dto;

import com.cuit.pay.orders.domain.Orders;
import lombok.Data;
import com.cuit.pay.orders.domain.Orders;
@Data
public class OrdersDTO extends Orders {
    /**
     * 订单创建人
     */
    private String name;
}
