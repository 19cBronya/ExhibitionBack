package com.cuit.pay.orders.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cuit.business.exhibitionHall.domain.dto.AgeData;
import com.cuit.business.exhibitionHall.domain.dto.MonthData;
import com.cuit.business.exhibitionHall.domain.dto.PersonData;
import com.cuit.common.common.R;
import com.cuit.pay.orders.domain.Orders;
import com.cuit.pay.orders.domain.dto.OrdersDTO;
import com.cuit.pay.orders.mapper.OrdersMapper;
import com.cuit.pay.orders.service.IOrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.hutool.core.util.RandomUtil;

import java.util.List;

@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements IOrdersService {

    @Autowired
    private OrdersMapper ordersMapper;
    @Override
    public boolean updateStatus(String traceNo, String payStatus, String gmtPayment, String alipayTradeNo) {
        return ordersMapper.updateStatus(traceNo,payStatus,gmtPayment,alipayTradeNo);
    }

    @Override
    public Orders getByOrderNo(String traceNo) {
        return ordersMapper.getByOrderNo(traceNo);
    }

    @Override
    public boolean updateStatus(String traceNo, String payStatus) {
        return ordersMapper.updateReturnStatus(traceNo,payStatus);
    }

    @Override
    public List<OrdersDTO> selectList(Long id,OrdersDTO orders) {
        return ordersMapper.selectListByPage(id,orders);
    }

    @Override
    public List<OrdersDTO> ordersPage(OrdersDTO dto) {
        return ordersMapper.ordersPage(dto);
    }

    @Override
    public List<OrdersDTO> selectListByIdList(Long id, OrdersDTO orders) {
        return ordersMapper.selectListByIdList(id,orders);
    }

    @Override
    public List<MonthData> selectMonthData(List<Long> eids) {
        return ordersMapper.selectMonthData(eids);
    }

    @Override
    public List<PersonData> selectPersonData(List<Long> eids) {
        return ordersMapper.selectPersonData(eids);
    }

    @Override
    public List<AgeData> selectAgeData(List<Long> eids) {
        return ordersMapper.selectAgeData(eids);
    }

    @Override
    public List<OrdersDTO> selectListByEidList(Long id, OrdersDTO orders) {
        return ordersMapper.selectListByEidList(id,orders);
    }

    @Override
    public List<OrdersDTO> selectListById(Long uid) {
        return ordersMapper.selectListById(uid);
    }
}
