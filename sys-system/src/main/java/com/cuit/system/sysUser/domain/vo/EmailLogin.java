package com.cuit.system.sysUser.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailLogin {

    /*角色名*/
    private String roleName;

    /*邮箱*/
    private String email;

    /*邮箱验证码*/
    private String emailCode;

}
