package com.cuit.business.exhibitionHall.domain.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AgeData {
    /**
     * 年龄段
     */
    private String age;
    /**
     * 数量
     */
    private BigDecimal num;

    /**
     * 占比率
     */
    private String rate;
}
