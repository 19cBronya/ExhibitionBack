package com.cuit.system.sysRoles.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.cuit.common.common.R;
import com.cuit.system.sysRoles.domain.SysRoles;

import java.util.List;

public interface ISysRolesService extends IService<SysRoles> {

    R deleteById(Long[] id);


    R forbiddenStatus(String status, Long[] id);

    R save(SysRoles sysRoles, boolean isCreate);

    List<SysRoles> selectSysRolesList(SysRoles sysRoles);
}
