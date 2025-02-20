package com.cuit.system.sysUserRoles.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.cuit.common.common.CommonConstants;
import com.cuit.common.common.R;
import com.cuit.system.base.BaseController;
import com.cuit.system.log.anno.Log;
import com.cuit.system.sysUser.domain.SysUser;
import com.cuit.system.sysUser.service.ISysUserService;
import com.cuit.system.sysUserRoles.domain.SysUserRoles;
import com.cuit.system.sysUserRoles.domain.dto.SysUserRolesDTO;
import com.cuit.system.sysUserRoles.service.ISysUserRolesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "角色分配控制类")
@Slf4j
@RestController
@RequestMapping("/userroles")
public class SysUserRolesController extends BaseController {

    @Autowired
    private ISysUserRolesService iSysUserRolesService;

    @Autowired
    private ISysUserService iSysUserService;


    /*分配角色*/
    @Log
    @ApiOperation("分配角色")
    @RequiresAuthentication
    @PostMapping("/assign")
    public R assignRole(@RequestBody SysUserRolesDTO sysUserRolesDTO){
        // 获取登录用户信息
        SysUserRoles sysUserRoles = getSysUserRoles();
        if (CommonConstants.UserRole.管理员.getValue().equals(sysUserRoles.getRid().toString())){
            if(exist(sysUserRolesDTO))
                return R.error(CommonConstants.ConstantsCode.错误.getValue(),"用户不存在");
            return iSysUserRolesService.updateRid(sysUserRolesDTO);
        }
        return R.error(CommonConstants.ConstantsCode.错误.getValue(),"权限不足，请联系管理员");
    }

    /*取消角色*/
    @Log
    @ApiOperation("取消角色")
    @RequiresAuthentication
    @PostMapping("/cancel")
    public R cancelRole(@RequestBody SysUserRolesDTO sysUserRolesDTO){
        // 获取登录用户信息
        SysUserRoles sysUserRoles = getSysUserRoles();
        if (CommonConstants.UserRole.管理员.getValue().equals(sysUserRoles.getRid().toString())){
            if(exist(sysUserRolesDTO))
                return R.error(CommonConstants.ConstantsCode.错误.getValue(),"用户不存在");
            return iSysUserRolesService.cancel(sysUserRolesDTO);
        }
        return R.error(CommonConstants.ConstantsCode.错误.getValue(),"权限不足，请联系管理员");
    }


    /*用户是否存在*/
    public boolean exist(SysUserRolesDTO sysUserRolesDTO){
        SysUser byId = iSysUserService.getById(sysUserRolesDTO.getUid());
        if(byId == null)
            return false;
        return true;
    }

}
