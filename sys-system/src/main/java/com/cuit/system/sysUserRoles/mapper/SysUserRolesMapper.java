package com.cuit.system.sysUserRoles.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cuit.system.sysUserRoles.domain.SysUserRoles;
import com.cuit.system.sysUserRoles.domain.dto.SysUserRolesDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysUserRolesMapper extends BaseMapper<SysUserRoles> {

    List<SysUserRoles> selectByRId(@Param("id") Long id);

    List<SysUserRolesDTO> selectSysUserRolesList(SysUserRolesDTO sysUserRolesDTO);

    List<Long> selectUid(String name);

    List<Long> selectRid(String roleName);

    String selectRoleNameByUid(Long id);

    String selectUserInfor(Long id);

    SysUserRoles selectByUId(Long id);

    List<SysUserRolesDTO> selectSysUserRolesAllList(SysUserRolesDTO sysUserRolesDTO);
}
