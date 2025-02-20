package com.cuit.admin.admin.user.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import com.cuit.system.base.BaseEntity;
import lombok.Data;

@Data
public class UserMg extends BaseEntity {

    @ExcelProperty(value = "账户名")
    private String loginName;
    @ExcelProperty(value = "用户名")
    private String name;
    @ExcelProperty(value = "角色名")
    private String roleName;
    @ExcelProperty(value = "性别")
    private String sex;
    @ExcelProperty(value = "电话")
    private String phone;
    @ExcelProperty(value = "邮箱")
    private String email;
    @ExcelProperty(value = "启用状态")
    private String status;

}
