package com.cuit.system.sysUser.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cuit.system.sysUser.domain.SysUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
    SysUser selectByid(Long uid);

    List<SysUser> selectByEmail(String email);

    String selectOrgNameById(Long id);

    Long selectOrgIdById(Long id);
}
