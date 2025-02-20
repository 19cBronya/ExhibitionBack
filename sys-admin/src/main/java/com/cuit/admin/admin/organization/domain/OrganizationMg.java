package com.cuit.admin.admin.organization.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class OrganizationMg {

    @ExcelProperty(value = "机构名称")
    private String organizationName;

    @ExcelProperty(value = "机构负责人")
    private String managerName;

    @ExcelProperty(value = "机构负责人联系方式")
    private String organizationPhone;

    @ExcelProperty(value = "机构负责人邮箱")
    private String organizationEmail;

    @ExcelProperty(value = "创建时间")
    private String createTime;
}
