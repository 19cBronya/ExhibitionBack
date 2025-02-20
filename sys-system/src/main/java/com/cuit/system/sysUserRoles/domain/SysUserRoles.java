package com.cuit.system.sysUserRoles.domain;


import com.cuit.system.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysUserRoles extends BaseEntity {

    /*用户id*/
    private Long uid;

    /*角色权限id*/
    private Long rid;

    /*状态标志 0正常 1禁用*/
    private String status;

    /*创建人id*/
    private Long createId;

    /*修改人id*/
    private Long updateId;
}
