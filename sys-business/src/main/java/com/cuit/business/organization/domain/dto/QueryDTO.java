package com.cuit.business.organization.domain.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.cuit.system.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class QueryDTO extends BaseEntity {

    /*搜索起始时间*/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelIgnore
    private String searchTimeS;

    /*搜索时间终止时间*/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ExcelIgnore
    private String searchTimeE;
}
