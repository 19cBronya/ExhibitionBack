package com.cuit.system.sysRoles.domain;


import com.cuit.system.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SysRoles extends BaseEntity {

    /*角色权限名称*/
    private String roleName;

    /*状态标志 0正常 1禁用*/
    private String status;

    /*创建人id*/
    private Long createId;

    /*修改人id*/
    private Long updateId;

}
