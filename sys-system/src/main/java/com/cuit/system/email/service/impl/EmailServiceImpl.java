package com.cuit.system.email.service.impl;


import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cuit.common.common.CommonConstants;
import com.cuit.common.common.R;
import com.cuit.common.common.ServiceException;
import com.cuit.common.util.RedisUtil;
import com.cuit.system.email.domain.Email;
import com.cuit.system.email.service.IEmailService;
import com.cuit.system.email.util.MailUtils;
import com.cuit.system.sysUser.domain.SysUser;
import com.cuit.system.sysUser.mapper.SysUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.List;

@Slf4j
@Service
public class EmailServiceImpl implements IEmailService {

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public String send(String email) throws MessagingException {
        List<SysUser> user = sysUserMapper.selectByEmail(email);
        if (user==null || user.size()<=0){
            String code = RandomUtil.randomNumbers(6);
            Email emailDto = new Email(email, "您的验证码为", code+"，请在5分钟内输入");
            MailUtils.sendMail(emailDto);
            redisUtil.set(email,code,300);
            return null;
        }else {
            return "该邮箱已被注册";
        }
    }

    @Override
    public void loginSend(String email) throws MessagingException {
        String code = RandomUtil.randomNumbers(6);
        Email emailDto = new Email(email, "您的登录验证码为", code+"，请在5分钟内输入");
        MailUtils.sendMail(emailDto);
        redisUtil.set(email,code,300);
    }
}


