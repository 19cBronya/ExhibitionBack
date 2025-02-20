package com.cuit.business.exhibition.domain.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cuit.business.exhibition.domain.Exhibition;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ExhibitionDTO extends Exhibition {

    /**
     * 办展机构名
     */
    private String organizationName;

    /*所属展馆名*/
    private String exhibitionHallName;

    /*地址*/
    private String address;

    /*经度*/
    private String longitude;

    /*纬度*/
    private String latitude;

    /**
     * 前台搜索时间 /年月日
     */
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8",shape = JsonFormat.Shape.STRING)
    private LocalDate searchTime;

    /**
     * 已出售票数
     */
    private Long soldTickets;
}
