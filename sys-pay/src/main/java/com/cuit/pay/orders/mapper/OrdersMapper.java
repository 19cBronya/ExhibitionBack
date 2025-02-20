package com.cuit.pay.orders.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cuit.business.exhibitionHall.domain.dto.AgeData;
import com.cuit.business.exhibitionHall.domain.dto.MonthData;
import com.cuit.business.exhibitionHall.domain.dto.PersonData;
import com.cuit.common.common.R;
import com.cuit.pay.orders.domain.Orders;
import com.cuit.pay.orders.domain.dto.OrdersDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrdersMapper extends BaseMapper<Orders> {

    boolean updateStatus(String traceNo, String payStatus, String gmtPayment, String alipayTradeNo);

    Orders getByOrderNo(String traceNo);

    boolean updateReturnStatus(String traceNo, String payStatus);

    List<OrdersDTO> selectListByPage(@Param("id") Long id, @Param("orders") OrdersDTO orders);

    List<OrdersDTO> ordersPage(OrdersDTO dto);

    List<OrdersDTO> selectListByIdList(@Param("id")Long id,@Param("orders") OrdersDTO orders);

    List<MonthData> selectMonthData(@Param("eids") List<Long> eids);

    List<PersonData> selectPersonData(@Param("eids") List<Long> eids);

    List<AgeData> selectAgeData(@Param("eids") List<Long> eids);

    List<OrdersDTO> selectListByEidList(@Param("id") Long id,@Param("orders") OrdersDTO orders);

    List<OrdersDTO> selectListById(Long uid);
}
