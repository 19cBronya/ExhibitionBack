package com.cuit.web.controller.login;

import com.cuit.common.common.CommonConstants;
import com.cuit.common.common.R;
import com.cuit.common.util.JWTUtils;
import com.cuit.common.util.RedisUtil;
import com.cuit.system.email.service.IEmailService;
import com.cuit.system.sysUser.domain.SysUser;
import com.cuit.system.sysUser.domain.vo.EmailLogin;
import com.cuit.system.sysUser.domain.vo.UserLogin;
import com.cuit.system.sysUser.service.ISysUserService;
import com.cuit.system.sysUserRoles.domain.dto.SysUserRolesDTO;
import com.cuit.system.sysUserRoles.service.ISysUserRolesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import com.cuit.system.log.anno.Log;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author xym
 * @description 登录控制层
 * @since 2023-11-07 10:30
 */

@Api(tags = "用户登录控制类")
@Slf4j
@RestController
@RequestMapping
public class SysLoginController {
    @Autowired
    private ISysUserService iSysUserService;

    @Autowired
    private ISysUserRolesService iSysUserRolesService;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private IEmailService iEmailService;

    /*图片路径补充*/
    @Value("${Image.path}")
    private String basePath;

    @Log
    @ApiOperation("登录控制层")
    @PostMapping("/login")
    public R login(@RequestBody UserLogin sysUser, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {

        //从session中取出验证码
        Object verifyCodeObj = request.getSession().getAttribute("verifyCode");
        if (verifyCodeObj == null) {
            return R.error(CommonConstants.ConstantsCode.错误.getValue(), "验证码不存在，请重新获取");
        }
        String verifyCode = verifyCodeObj.toString();

        if (sysUser.getIdentifyCode().equalsIgnoreCase(verifyCode)) {
            SysUser user = iSysUserService.login(sysUser);

            if (user == null) {
                return R.error(CommonConstants.ConstantsCode.错误.getValue(), "用户名或密码错误");
            }

            String roleName = iSysUserRolesService.selectRoleNameByUid(user.getId());

            if (CommonConstants.Status.禁用.getValue().equals(user.getStatus())) {
                return R.error(CommonConstants.ConstantsCode.错误.getValue(), "登录失败，该账户已被禁用");
            }
            if (CommonConstants.Status.审核中.getValue().equals(user.getStatus())) {
                return R.error(CommonConstants.ConstantsCode.错误.getValue(), "登录失败，该账户在审核中");
            }

            if (!CommonConstants.UserRole.管理员.getValue().contains(roleName)&&!roleName.contains(sysUser.getRoleName())) {
                return R.error(CommonConstants.ConstantsCode.错误.getValue(), "登录失败，该账户没有该角色权限");
            }

            String token = JWTUtils.createToken(user.getId());
            HashMap<String, Object> map = new HashMap<>();
            map.put("token", token);
            map.put("user", user);
            map.put("roleName", roleName);
            return R.success("登录成功", map);
        } else {
            return R.error(CommonConstants.ConstantsCode.错误.getValue(), "验证码错误,请重新输入");
        }
    }

    /*注销登录*/
    @Log
    @ApiOperation("注销登录")
    @RequiresAuthentication
    @PostMapping("/logout")
    public R logout() {
        SysUser user = (SysUser) SecurityUtils.getSubject().getPrincipal();
        redisUtil.hdel("refreshToken", user.getId().toString());
        redisUtil.hdel("userInfo", user.getId().toString());
        SecurityUtils.getSubject().logout();
        //System.out.println(user);
        return R.success("注销成功");
    }

    @com.cuit.system.log.anno.Log
    @ApiOperation("token认证出错返回请求")
    //在JWTFilter中认证出错会跳转到这统一进行返回
    @GetMapping("/unauthorized")
    public R unauthorized(String message) throws UnsupportedEncodingException {
        return R.error(CommonConstants.ConstantsCode.错误.getValue(), message);
    }

    /*获取当前用户信息及其角色信息*/
    @Log
    @ApiOperation("获取当前用户信息及其角色信息")
    @PostMapping("/get")
    @RequiresAuthentication
    public R getUserInfor() {
        SysUser sysUser = (SysUser) SecurityUtils.getSubject().getPrincipal();
        SysUserRolesDTO sysUserRolesDTO = new SysUserRolesDTO();
        String roleName = iSysUserRolesService.selectUserInfor(sysUser.getId());
        List<SysUser> user = new ArrayList<>();
        SysUser sysUser1 = iSysUserService.selectByid(sysUser.getId());
        user.add(sysUser1);
        sysUserRolesDTO.setUser(user);
        sysUserRolesDTO.setRoleName(roleName);
        return R.success("查询成功", sysUserRolesDTO);
    }

    @Log
    @ApiOperation("邮箱登录控制层")
    @PostMapping("/email/login")
    public R emaiLogin(@RequestBody EmailLogin sysUser, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {

        if (!Pattern.matches
                ("^(\\w+([-.][A-Za-z0-9]+)*){3,18}@\\w+([-.][A-Za-z0-9]+)*\\.\\w+([-.][A-Za-z0-9]+)*$"
                        , sysUser.getEmail())){
            return R.error("邮箱格式不正确");
        }
        String code = (String) redisUtil.get(sysUser.getEmail());
        if (code == null || !code.equals(sysUser.getEmailCode())){
            return R.error("邮箱验证码错误,请重新输入");
        }


            SysUser user = iSysUserService.emailLogin(sysUser);

            if(user==null){
                return R.error("登录失败,邮箱不存在");
            }

            String roleName  = iSysUserRolesService.selectRoleNameByUid(user.getId());

            if (CommonConstants.Status.禁用.toString().equals(user.getStatus())){
                return R.error(CommonConstants.ConstantsCode.错误.getValue(),"该账户已被禁用");
            }
            if (CommonConstants.Status.审核中.toString().equals(user.getStatus())){
                return R.error(CommonConstants.ConstantsCode.错误.getValue(),"该账户在审核中");
            }
            if (!CommonConstants.UserRole.管理员.toString().contains(roleName)&&!roleName.contains(sysUser.getRoleName())) {
                return R.error(CommonConstants.ConstantsCode.错误.getValue(), "登录失败，该账户没有该角色权限");
            }

            String token = JWTUtils.createToken(user.getId());
            HashMap<String, Object> map = new HashMap<>();
            map.put("token",token);
            map.put("user",user);
            map.put("roleName",roleName);
            return R.success("登录成功",map);
        }

    /*发送验证码*/
    @Log
    @ApiOperation("发送验证码")
    @PostMapping("/login/sendCode")
    public R sendCode(@RequestParam String email) throws MessagingException {
        if ((email != null) && (!email.isEmpty())) {
            if (Pattern.matches("^(\\w+([-.][A-Za-z0-9]+)*){3,18}@\\w+([-.][A-Za-z0-9]+)*\\.\\w+([-.][A-Za-z0-9]+)*$", email)){
                iEmailService.loginSend(email);
            }else {
                return R.error("邮箱格式不正确");
            }
        }else {
            return R.error("邮箱不能为空");
        }
        return R.success("验证码发送成功");
    }

}
