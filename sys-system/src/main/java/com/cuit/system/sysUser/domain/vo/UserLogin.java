package com.cuit.system.sysUser.domain.vo;

import com.cuit.system.sysUser.domain.SysUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLogin extends SysUser {
    private String roleName;
    /*登录验证码*/
    private String identifyCode;
}
