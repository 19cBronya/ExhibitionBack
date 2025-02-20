package com.cuit.business.exhibitionHall.domain.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PersonData {
    /**
     * 周数
     */
    private String week;

    /**
     * 销量
     */
    private BigDecimal num;
}
