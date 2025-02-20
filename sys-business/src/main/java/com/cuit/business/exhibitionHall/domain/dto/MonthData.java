package com.cuit.business.exhibitionHall.domain.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MonthData {
    /**
     * 月份
     */
    private String month;

    /**
     * 销量数据
     */
    private BigDecimal num;
}
