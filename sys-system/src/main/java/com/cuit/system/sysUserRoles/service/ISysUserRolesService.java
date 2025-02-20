package com.cuit.system.sysUserRoles.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.cuit.common.common.R;
import com.cuit.system.sysUserRoles.domain.SysUserRoles;
import com.cuit.system.sysUserRoles.domain.dto.SysUserRolesDTO;

import java.util.List;

public interface ISysUserRolesService extends IService<SysUserRoles> {

    List<SysUserRolesDTO> selectSysUserRolesList(SysUserRolesDTO sysUserRolesDTO);

    List<Long> selectUid(String name);

    List<Long> selectRid(String roleName);

    R updateRid(SysUserRolesDTO sysUserRolesDTO);

    R cancel(SysUserRolesDTO sysUserRolesDTO);
    String selectRoleNameByUid(Long id);

    String selectUserInfor(Long id);

    List<SysUserRolesDTO> selectSysUserRolesAllList(SysUserRolesDTO sysUserRolesDTO);
}
