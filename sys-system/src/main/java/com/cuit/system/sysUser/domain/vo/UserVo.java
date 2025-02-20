package com.cuit.system.sysUser.domain.vo;


import com.cuit.system.sysUser.domain.SysUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVo extends SysUser {

    /*角色id 1普通观众 2专业观众 3参展商 4管理员*/
    private Long rId;
    /*验证码*/
    private String code;

}
