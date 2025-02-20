package com.cuit.admin.admin.exhibitor.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class ExhibitiorMg {
    /**
     * 参展商负责人名
     */
    @ExcelProperty(value = "参展商负责人名")
    private String exhibitorName;

    /*公司名称*/
    @ExcelProperty(value = "公司名称")
    private String companyName;

    /*公司介绍*/
    @ExcelProperty(value = "公司介绍")
    private String companyInfo;

}
