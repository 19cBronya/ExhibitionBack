package com.cuit.system.sysRoles.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cuit.common.common.CommonConstants;
import com.cuit.common.common.R;
import com.cuit.system.sysRoles.domain.SysRoles;
import com.cuit.system.sysRoles.mapper.SysRolesMapper;
import com.cuit.system.sysRoles.service.ISysRolesService;
import com.cuit.system.sysUser.domain.SysUser;
import com.cuit.system.sysUser.mapper.SysUserMapper;
import com.cuit.system.sysUserRoles.domain.SysUserRoles;
import com.cuit.system.sysUserRoles.mapper.SysUserRolesMapper;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class SysRolesServiceImpl extends ServiceImpl<SysRolesMapper, SysRoles> implements ISysRolesService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private SysRolesMapper sysRolesMapper;

    @Autowired
    private SysUserRolesMapper sysUserRolesMapper;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public R deleteById(Long[] id) {

        LambdaQueryWrapper<SysRoles> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(id != null, SysRoles::getId, id);
        List<SysRoles> list = this.list(queryWrapper);
        List<SysRoles> failedRoles = new ArrayList<>();

        for (SysRoles sysRoles : list) {
            String status = sysRoles.getStatus();
            if (!CommonConstants.Status.启用.getValue().equals(status)) {
                failedRoles.add(sysRoles);
                if (CommonConstants.UserRole.管理员.getValue().equals(sysRoles.getId().toString()) ||
                        CommonConstants.UserRole.展览馆.getValue().equals(sysRoles.getId().toString()) ||
                        CommonConstants.UserRole.办展机构.getValue().equals(sysRoles.getId().toString()) ||
                        CommonConstants.UserRole.普通观众.getValue().equals(sysRoles.getId().toString()) ||
                        CommonConstants.UserRole.专业观众.getValue().equals(sysRoles.getId().toString()) ||
                        CommonConstants.UserRole.参展商.getValue().equals(sysRoles.getId().toString())) {
                    return R.error(CommonConstants.ConstantsCode.错误.getValue(),"删除失败，不允许对基本角色进行操作");
                }
            } else {
                return R.error(CommonConstants.ConstantsCode.错误.getValue(),"删除失败，请先禁用所选角色");
            }
        }
         /*批量更新SysRoles对象delFlag属性*/
        for (SysRoles failedRole : failedRoles) {

            /*记录操作人id*/
            failedRole.setUpdateId(((SysUser) SecurityUtils.getSubject().getPrincipal()).getId());
            failedRole.setDelFlag(CommonConstants.DeleteFlag.删除.getValue());
            int updateCount0 = sysRolesMapper.updateById(failedRole);

            if (updateCount0 < 0)
                return R.error(CommonConstants.ConstantsCode.错误.getValue(),"删除角色失败");

            /*原本角色删除后就改为默认的普通观众*/
            if (getUR(failedRole))
                return R.error(CommonConstants.ConstantsCode.错误.getValue(), "更新失败，请联系管理员");

        }
        return R.success("删除成功");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public R forbiddenStatus(String status, Long[] id) {

        LambdaQueryWrapper<SysRoles> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(id != null, SysRoles::getId, id);
        List<SysRoles> list = this.list(queryWrapper);
        List<SysRoles> failedRoles = new ArrayList<>();

        for (SysRoles sysRoles : list) {
                failedRoles.add(sysRoles);
                if (CommonConstants.UserRole.管理员.getValue().equals(sysRoles.getId().toString()) ||
                        CommonConstants.UserRole.展览馆.getValue().equals(sysRoles.getId().toString()) ||
                        CommonConstants.UserRole.办展机构.getValue().equals(sysRoles.getId().toString()) ||
                        CommonConstants.UserRole.普通观众.getValue().equals(sysRoles.getId().toString()) ||
                        CommonConstants.UserRole.专业观众.getValue().equals(sysRoles.getId().toString()) ||
                        CommonConstants.UserRole.参展商.getValue().equals(sysRoles.getId().toString())) {
                    return R.error(CommonConstants.ConstantsCode.错误.getValue(),"禁用失败，不允许对基本角色进行操作");
                }
        }
        /*批量更新SysRoles对象status属性*/
        for (SysRoles failedRole : failedRoles){

            /*记录操作人id*/
            failedRole.setUpdateId(((SysUser) SecurityUtils.getSubject().getPrincipal()).getId());

            if (!failedRole.getStatus().equals(status)) {
                failedRole.setStatus(status);
            }
            int forbiddenCount0 = sysRolesMapper.updateById(failedRole);

            if (forbiddenCount0 < 0)
                return R.error(CommonConstants.ConstantsCode.错误.getValue(),"禁用角色失败");

            /*原本角色禁用后就改为默认的普通观众*/
            if (getUR(failedRole))
                return R.error(CommonConstants.ConstantsCode.错误.getValue(), "更新失败，请联系管理员");

        }
        if(status.equals(CommonConstants.Status.启用.getValue()))
        return R.success("启用成功");
        return R.success("禁用成功");
    }

    @Override
    public R save(SysRoles sysRoles, boolean isCreate) {
        Integer count = sysRolesMapper.selectCount(new LambdaQueryWrapper<SysRoles>().eq(SysRoles::getRoleName, sysRoles.getRoleName()));
        if (count != 0) {
            return R.error(CommonConstants.ConstantsCode.错误.getValue(), "角色名已存在，请重新输入");
        }

        SysUser currentUser = (SysUser) SecurityUtils.getSubject().getPrincipal();
        sysRoles.setUpdateId(currentUser.getId());

        if (isCreate) {
            sysRoles.setCreateId(currentUser.getId());
            int insertResult = sysRolesMapper.insert(sysRoles);
            if (insertResult == 1) {
                return R.success("新增成功");
            }
        } else {
            int updateResult = sysRolesMapper.updateById(sysRoles);
            if (updateResult == 1) {
                return R.success("修改成功");
            }
        }

        return R.error(CommonConstants.ConstantsCode.错误.getValue(), "操作失败，请联系管理员");
    }

    @Override
    public List<SysRoles> selectSysRolesList(SysRoles sysRoles) {
        return sysRolesMapper.selectSysRolesList(sysRoles);
    }


    /*原本角色删除/禁用后就改为默认的普通观众*/
    private boolean getUR(SysRoles failedRole) {
        List<SysUserRoles> sysUserRoles = sysUserRolesMapper.selectByRId(failedRole.getId());
        if(sysUserRoles.size()>0){
            for (SysUserRoles sysUserRole : sysUserRoles) {

                sysUserRole.setUpdateId(((SysUser) SecurityUtils.getSubject().getPrincipal()).getId());

                sysUserRole.setRid(Long.valueOf(CommonConstants.UserRole.普通观众.getValue()));
                int updateCount1 = sysUserRolesMapper.updateById(sysUserRole);
                if (updateCount1 < 0) {
                    return true;
                }
            }
        }
        return false;
    }
}
