package com.cuit.system.sysUserRoles.domain.dto;



import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cuit.system.sysUser.domain.SysUser;
import com.cuit.system.sysUserRoles.domain.SysUserRoles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_user_roles")
public class SysUserRolesDTO extends SysUserRoles {

    /*用户姓名*/
    private String name;

    /*角色名*/
    @ExcelProperty(value = "角色名")
    private String roleName;

    /*用户信息*/
    private List<SysUser> user;

}
