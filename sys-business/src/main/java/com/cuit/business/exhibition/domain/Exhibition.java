package com.cuit.business.exhibition.domain;

import com.cuit.system.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Exhibition extends BaseEntity {

    /*展览馆主键id*/
    private Long hid;

    /*展会名称*/
    private String exhibitionName;

    /*展会图片*/
    private String exhibitionUrl;

    /*展会类型*/
    private String exhibitionType;

    /*展位数量*/
    private Integer exhibitionNum;

    /*单张票价 （单位：元）*/
    private BigDecimal ticketPrice;

    /*总票数*/
    public Long totalVotes;

    /*总收入*/
    private BigDecimal totalIncome;

    /*总流量*/
    private Long totalTraffic;

    /*开始时间*/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8",shape = JsonFormat.Shape.STRING)
    private LocalDateTime openingTime;

    /*截止时间*/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8",shape = JsonFormat.Shape.STRING)
    private LocalDateTime closingTime;

    /*状态标志 0正常 1禁用 ，默认为1 需审批*/
    private String status;

    /*创建人id*/
    private Long createId;

    /*修改人id*/
    private Long updateId;
}
