package com.cuit.system.sysUser.controller;


import com.cuit.common.common.CommonConstants;
import com.cuit.common.common.R;
import com.cuit.common.util.Md5Utils;
import com.cuit.common.util.RedisUtil;
import com.cuit.system.base.BaseController;
import com.cuit.system.log.anno.Log;
import com.cuit.system.sysUser.domain.SysUser;
import com.cuit.system.sysUser.domain.dto.SysUserDTO;
import com.cuit.system.sysUser.service.ISysUserService;
import com.cuit.system.sysUserRoles.domain.SysUserRoles;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author xym
 * @since 2023-11-08 10:22
 * @description 用户信息管理
 */
@Api(tags = "个人信息管理控制类")
@Slf4j
@RestController
@RequestMapping("/user")
public class SysUserController extends BaseController {

    @Autowired
    private ISysUserService iSysUserService;

    @Autowired
    private RedisUtil redisUtil;

    /*盐值*/
    @Value("${Salt.value}")
    private String salt;

    /*图片路径补充*/
    @Value("${Image.path}")
    private String basePath;

    /*个人信息展示*/
    @Log
    @ApiOperation("个人信息展示")
    @RequiresAuthentication
    @PostMapping("/inforList")
    public R getInforList(){
        SysUserRoles sysUser = getSysUserRoles();
        if(sysUser==null)
            return R.error("请先登录");
        Long id = sysUser.getUid();
        SysUser user = iSysUserService.getById(id);
        /*头像图片显示前缀*/
        if (user.getAvatarUrl() != null) {
            if(!user.getAvatarUrl().contains(basePath))
                user.setAvatarUrl(basePath + user.getAvatarUrl());
        }
        SysUserDTO dto = new SysUserDTO();
        BeanUtils.copyProperties(user,dto);
        if(CommonConstants.UserRole.办展机构.getValue().equals(String.valueOf(sysUser.getRid()))){
            Long orgId = iSysUserService.selectOrgIdById(user.getId());
            if (orgId != null)
                dto.setOrgId(orgId);
            String orgName = iSysUserService.selectById(user.getId());
            if (orgName != null)
            dto.setOrgName(orgName);
        }
        return R.success("查询成功",dto);
    }

    /*个人信息编辑修改*/
    @Log
    @ApiOperation("个人信息编辑修改")
    @RequiresAuthentication
    @PostMapping("/updateInfor")
    public R updateSysUser(@RequestBody SysUser sysUser) {
        if (sysUser == null) return R.error(CommonConstants.ConstantsCode.错误.getValue(),"修改数据不能为空，请重新输入");
        SysUser user_now = (SysUser) SecurityUtils.getSubject().getPrincipal();

        // 更新数据
        BeanUtils.copyProperties(sysUser, user_now, getNullPropertyNames(sysUser));

        // 校验电话号码格式
        String phoneRegex = "^((13[0-9])|(14[5-9])|(15([0-3]|[5-9]))|(16[6-7])|(17[1-8])|(18[0-9])|(19[1|3])|(19[5|6])|(19[8|9]))\\d{8}$";
        Pattern phonePattern = Pattern.compile(phoneRegex);
        Matcher phoneMatcher = phonePattern.matcher(user_now.getPhone());
        if (!phoneMatcher.matches()) {
            return R.error(CommonConstants.ConstantsCode.错误.getValue(),"电话号码格式不正确");
        }

        // 校验邮箱格式
        String emailRegex = "^(\\w+([-.][A-Za-z0-9]+)*){3,18}@\\w+([-.][A-Za-z0-9]+)*\\.\\w+([-.][A-Za-z0-9]+)*$";
        Pattern emailPattern = Pattern.compile(emailRegex);
        Matcher emailMatcher = emailPattern.matcher(user_now.getEmail());
        if (!emailMatcher.matches()) {
            return R.error(CommonConstants.ConstantsCode.错误.getValue(),"邮箱格式不正确");
        }

        boolean flag1 = iSysUserService.updateById(user_now);
        if (!flag1) {
            return R.error(CommonConstants.ConstantsCode.错误.getValue(),"更新失败");
        }

        return R.success("信息编辑成功");
    }

    /*个人密码修改*/
    @Log
    @ApiOperation(value = "修改密码")
    @RequiresAuthentication
    @PostMapping("/updatePassword")
    public R updatePassword(@RequestParam("oldPassword") String oldPassword, @RequestParam("newPassword") String newPassword) {
        SysUser principal = (SysUser) SecurityUtils.getSubject().getPrincipal();
        //原密码输入错误，则不允许修改
        SysUser user = iSysUserService.getById(principal.getId());
        String password = Md5Utils.encryption(oldPassword,salt);
        if (!user.getPassword().equals(password))
            return R.error(CommonConstants.ConstantsCode.错误.getValue(),"原密码错误,无法修改");
        //设置新密码
        user.setPassword(Md5Utils.encryption(newPassword,salt));
        boolean flag = iSysUserService.updateById(user);
        if(!flag){
            return R.error(CommonConstants.ConstantsCode.错误.getValue(),"修改密码失败，清联系管理员");
        }

        // 注销当前用户，使用户主体内容失效
        SecurityUtils.getSubject().logout();
        redisUtil.hdel("refreshToken",user.getId().toString());
        redisUtil.hdel("userInfo",user.getId().toString());
        return R.success("密码修改成功,请重新登录");
    }
}
