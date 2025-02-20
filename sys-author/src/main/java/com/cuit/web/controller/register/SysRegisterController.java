package com.cuit.web.controller.register;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cuit.common.common.CommonConstants;
import com.cuit.common.common.R;
import com.cuit.system.email.service.IEmailService;
import com.cuit.system.log.anno.Log;
import com.cuit.system.sysUser.domain.SysUser;
import com.cuit.system.sysUser.domain.vo.UserVo;
import com.cuit.system.sysUser.service.ISysUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.util.regex.Pattern;

/**
 * @author xym
 * @since 2023-11-07 14:33
 * @description 用户信息注册层
 */

@Api(tags = "用户注册控制类")
@Slf4j
@RestController
@RequestMapping
public class SysRegisterController {

    @Autowired
    private IEmailService iEmailService;

    @Autowired
    private ISysUserService iSysUserService;

    /*邮箱注册*/
    @Log
    @ApiOperation("邮箱注册控制层")
    @PostMapping("/register")
    public R register(@RequestBody UserVo user){
        if ((user.getEmail() != null) && (!user.getEmail().isEmpty())) {
            if (Pattern.matches("^(\\w+([-.][A-Za-z0-9]+)*){3,18}@\\w+([-.][A-Za-z0-9]+)*\\.\\w+([-.][A-Za-z0-9]+)*$", user.getEmail())){
                String msg = iSysUserService.register(user);
                if (msg!=null)
                    return R.error(msg);
            }else {
                return R.error("邮箱格式不正确");
            }
        }else {
            return R.error("邮箱不能为空");
        }
            return R.success("注册成功");
    }

    /*发送验证码*/
    @Log
    @ApiOperation("发送验证码")
    @PostMapping("/sendCode")
    public R sendCode(@RequestParam String email) throws MessagingException {
        if ((email != null) && (!email.isEmpty())) {
            if (Pattern.matches("^(\\w+([-.][A-Za-z0-9]+)*){3,18}@\\w+([-.][A-Za-z0-9]+)*\\.\\w+([-.][A-Za-z0-9]+)*$", email)){
                String msg = iEmailService.send(email);
                if (msg!=null)
                    return R.error(msg);
            }else {
                return R.error("邮箱格式不正确");
            }
        }else {
            return R.error("邮箱不能为空");
        }
        return R.success("验证码发送成功");
    }

    /*校验用户名是否存在*/
    @ApiOperation("检验用户名是否已存在")
    @GetMapping("/check")
    public R countByUsername(@RequestParam("loginName") String loginName) {
        long count = iSysUserService.count(new LambdaQueryWrapper<SysUser>().eq(SysUser::getLoginName, loginName));
        if(count!=0){
            return R.error(CommonConstants.ConstantsCode.错误.getValue(),"用户名已存在");
        }
        return R.success(null);
    }

}
