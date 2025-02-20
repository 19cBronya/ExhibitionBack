package com.cuit.system.email.service;

import com.cuit.common.common.R;

import javax.mail.MessagingException;

/**
 * @author xym
 * @since 2023-11-07 11:09
 * @description 邮箱服务接口
 */

public interface IEmailService {

    /**
     * 发送邮件
     * @param email
     */
    String send(String email) throws MessagingException;

    void loginSend(String email) throws MessagingException;
}
