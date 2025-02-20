package com.cuit.system.sysUser.domain;


import com.cuit.system.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysUser extends BaseEntity {

    /*登录账号*/
    private String loginName;

    /*登录密码*/
    private String password;

    /*头像*/
    private String avatarUrl;

    /*用户名*/
    private String name;

    /*年龄*/
    private Integer age;

    /*性别*/
    private String sex;

    /*电话号码*/
    private String phone;

    /*邮箱*/
    private String email;

    /*状态标志 0正常 1禁用*/
    private String status;
}
