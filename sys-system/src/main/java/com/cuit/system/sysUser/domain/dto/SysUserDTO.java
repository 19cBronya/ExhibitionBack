package com.cuit.system.sysUser.domain.dto;

import com.cuit.system.sysUser.domain.SysUser;
import lombok.Data;

@Data
public class SysUserDTO extends SysUser {
    private Long orgId;
    private String orgName;
}
