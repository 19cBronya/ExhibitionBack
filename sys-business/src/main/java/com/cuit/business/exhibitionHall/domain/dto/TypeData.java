package com.cuit.business.exhibitionHall.domain.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 展会类型数据分析
 */
@Data
public class TypeData {
    /**
     * 类型名
     */
    private String typeName;
    /**
     * 类型数
     */
    private BigDecimal typeNum;
}
