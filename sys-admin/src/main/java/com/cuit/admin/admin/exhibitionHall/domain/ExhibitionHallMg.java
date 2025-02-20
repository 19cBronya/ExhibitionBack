package com.cuit.admin.admin.exhibitionHall.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class ExhibitionHallMg {
    @ExcelProperty(value = "展馆名")
    private String name;

    @ExcelProperty(value = "展馆负责人")
    private String manageName;

    @ExcelProperty(value = "展览馆地址")
    private String address;

    @ExcelProperty(value = "展馆最大容量数")
    private String capacityNumber;

    @ExcelProperty(value = "开馆时间")
    private String openingTime;

    @ExcelProperty(value = "展厅最大容量数")
    private String closingTime;

    @ExcelProperty(value = "展馆状态")
    private String status;

    @ExcelProperty(value = "创建时间")
    private String createTime;


}
