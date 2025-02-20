package com.cuit.system.sysRoles.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cuit.common.common.CommonConstants;
import com.cuit.common.common.R;
import com.cuit.common.common.page.TableDataInfo;
import com.cuit.system.base.BaseController;
import com.cuit.system.log.anno.Log;
import com.cuit.system.sysRoles.domain.SysRoles;
import com.cuit.system.sysRoles.service.ISysRolesService;
import com.cuit.system.sysUser.domain.SysUser;
import com.cuit.system.sysUserRoles.domain.SysUserRoles;
import com.cuit.system.sysUserRoles.service.ISysUserRolesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Api(tags = "角色权限控制类")
@Slf4j
@RestController
@RequestMapping("/roles")
public class SysRolesController extends BaseController {

    @Autowired
    private ISysUserRolesService iSysUserRolesService;

    @Autowired
    private ISysRolesService iSysRolesService;

    /*查询角色权限列表*/
    @Log
    @ApiOperation("分页查询角色")
    @RequiresAuthentication
    @PostMapping("/page")
    public R<TableDataInfo> page(@RequestBody SysRoles sysRoles) {
        // 获取登录用户信息
        SysUserRoles sysUserRoles = getSysUserRoles();
        if (CommonConstants.UserRole.管理员.getValue().equals(sysUserRoles.getRid().toString())){
            startPage(sysRoles);
            List<SysRoles> list = iSysRolesService.selectSysRolesList(sysRoles);
            return R.success("查询成功",getDataTable(list));

        }
        return R.error(CommonConstants.ConstantsCode.错误.getValue(),"权限不足，请联系管理员");
    }

    /*新增 || 编辑角色权限*/
    @Log
    @ApiOperation("新增||编辑角色")
    @RequiresAuthentication
    @PutMapping()
    public R update(@RequestBody SysRoles sysRoles){
        // 获取登录用户信息
        SysUserRoles sysUserRoles = getSysUserRoles();
        if(CommonConstants.UserRole.管理员.getValue().equals(sysUserRoles.getRid().toString())){
            boolean isCreate = false;
            if(sysRoles.getId() == null || sysRoles.getId() == 0L){
                isCreate = true;
            }
            return iSysRolesService.save(sysRoles,isCreate);
        }
        return R.error(CommonConstants.ConstantsCode.错误.getValue(),"权限不足，请联系管理员");
    }



    /*禁用角色权限*/
    @Log
    @ApiOperation("禁用角色")
    @RequiresAuthentication
    @PostMapping("/changeStatus/{status}")
    public R forbiddenRoles(@PathVariable String status, @RequestParam("id") String[] id){
        // 获取登录用户信息
        SysUserRoles sysUserRoles = getSysUserRoles();
        if(CommonConstants.UserRole.管理员.getValue().equals(sysUserRoles.getRid().toString())){
            Long[] longIds = Arrays.stream(id)
                    .mapToLong(Long::parseLong)
                    .boxed()
                    .toArray(Long[]::new);
            return iSysRolesService.forbiddenStatus(status,longIds);
        }
        else
            return R.error(CommonConstants.ConstantsCode.错误.getValue(),"权限不足，请联系管理员");
    }



    /*删除角色权限*/
    @Log
    @ApiOperation("删除角色")
    @RequiresAuthentication
    @DeleteMapping
    public R deleteRoles(@RequestParam("id") String[] id) throws Exception {
        // 获取登录用户相关信息
        SysUserRoles sysUserRoles = getSysUserRoles();
        if(CommonConstants.UserRole.管理员.getValue().equals(sysUserRoles.getRid().toString())){
            Long[] longIds = Arrays.stream(id)
                    .mapToLong(Long::parseLong)
                    .boxed()
                    .toArray(Long[]::new);
            return iSysRolesService.deleteById(longIds);
        }
        else
            return R.error(CommonConstants.ConstantsCode.错误.getValue(),"权限不足，请联系管理员");

    }



}
