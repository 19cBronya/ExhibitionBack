package com.cuit.system.sysRoles.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cuit.system.sysRoles.domain.SysRoles;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysRolesMapper extends BaseMapper<SysRoles> {
    int forbiddenById(@Param("status")String status, SysRoles failedRole);

    List<SysRoles> selectSysRolesList(SysRoles sysRoles);
}
