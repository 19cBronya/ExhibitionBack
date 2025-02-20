package com.cuit.system.sysUserRoles.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cuit.common.common.CommonConstants;
import com.cuit.common.common.R;
import com.cuit.system.sysUser.domain.SysUser;
import com.cuit.system.sysUserRoles.domain.SysUserRoles;
import com.cuit.system.sysUserRoles.domain.dto.SysUserRolesDTO;
import com.cuit.system.sysUserRoles.mapper.SysUserRolesMapper;
import com.cuit.system.sysUserRoles.service.ISysUserRolesService;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SysUserRolesServiceImpl extends ServiceImpl<SysUserRolesMapper, SysUserRoles> implements ISysUserRolesService {

    @Autowired
    private SysUserRolesMapper sysUserRolesMapper;

    @Override
    public List<SysUserRolesDTO> selectSysUserRolesList(SysUserRolesDTO sysUserRolesDTO) {
        return sysUserRolesMapper.selectSysUserRolesList(sysUserRolesDTO);
    }

    @Override
    public List<Long> selectUid(String name) {
        return sysUserRolesMapper.selectUid(name);
    }

    @Override
    public List<Long> selectRid(String roleName) {
        return sysUserRolesMapper.selectRid(roleName);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R updateRid(SysUserRolesDTO sysUserRolesDTO) {
        SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
        if(!CommonConstants.UserRole.参展商.getValue().equals(sysUserRolesDTO.getRid().toString())&&
                !CommonConstants.UserRole.办展机构.getValue().equals(sysUserRolesDTO.getRid().toString())&&
                !CommonConstants.UserRole.展览馆.getValue().equals(sysUserRolesDTO.getRid().toString())&&
                !CommonConstants.UserRole.管理员.getValue().equals(sysUserRolesDTO.getRid().toString())){
            sysUserRolesDTO.setUpdateId(user.getId());
            sysUserRolesMapper.updateById(sysUserRolesDTO);
            return R.success("操作成功");
        }
        return R.error(CommonConstants.ConstantsCode.错误.getValue(),"禁止操作");
    }

    @Override
    public R cancel(SysUserRolesDTO sysUserRolesDTO) {
        SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
        if(!CommonConstants.UserRole.参展商.getValue().equals(sysUserRolesDTO.getRid().toString())&&
                !CommonConstants.UserRole.办展机构.getValue().equals(sysUserRolesDTO.getRid().toString())&&
                !CommonConstants.UserRole.展览馆.getValue().equals(sysUserRolesDTO.getRid().toString())&&
                !CommonConstants.UserRole.管理员.getValue().equals(sysUserRolesDTO.getRid().toString())) {
            sysUserRolesDTO.setUpdateId(user.getId());
            sysUserRolesDTO.setRid(Long.valueOf(CommonConstants.UserRole.普通观众.getValue()));
            sysUserRolesMapper.updateById(sysUserRolesDTO);
            return R.success("操作成功");
        }
        return R.error(CommonConstants.ConstantsCode.错误.getValue(),"禁止操作");
    }

    @Override
    public String selectRoleNameByUid(Long id) {
        return sysUserRolesMapper.selectRoleNameByUid(id);
    }

    @Override
    public String selectUserInfor(Long id) {
        return sysUserRolesMapper.selectUserInfor(id);
    }

    @Override
    public List<SysUserRolesDTO> selectSysUserRolesAllList(SysUserRolesDTO sysUserRolesDTO) {
        return sysUserRolesMapper.selectSysUserRolesAllList(sysUserRolesDTO);
    }

}
