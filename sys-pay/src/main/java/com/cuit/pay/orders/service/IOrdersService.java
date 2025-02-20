package com.cuit.pay.orders.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.cuit.business.exhibitionHall.domain.dto.AgeData;
import com.cuit.business.exhibitionHall.domain.dto.MonthData;
import com.cuit.business.exhibitionHall.domain.dto.PersonData;
import com.cuit.common.common.R;
import com.cuit.pay.orders.domain.Orders;
import com.cuit.pay.orders.domain.dto.OrdersDTO;

import java.util.List;

public interface IOrdersService extends IService<Orders> {
    boolean updateStatus(String traceNo, String payStatus, String gmtPayment, String alipayTradeNo);

    Orders getByOrderNo(String traceNo);

    boolean updateStatus(String traceNo, String payStatus);

    List<OrdersDTO> selectList(Long id,OrdersDTO orders);

    List<OrdersDTO> ordersPage(OrdersDTO dto);

    List<OrdersDTO> selectListByIdList(Long id, OrdersDTO orders);

    List<MonthData> selectMonthData(List<Long> eids);

    List<PersonData> selectPersonData(List<Long> eids);

    List<AgeData> selectAgeData(List<Long> eids);

    List<OrdersDTO> selectListByEidList(Long id, OrdersDTO orders);

    List<OrdersDTO> selectListById(Long uid);
}
