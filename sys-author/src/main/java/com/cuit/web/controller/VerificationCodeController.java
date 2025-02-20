package com.cuit.web.controller;


import com.cuit.common.common.R;
import com.cuit.system.log.anno.Log;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

/**
 * @author xym
 * @since 2023-11-08 09:24
 * @description 验证码控制管理
 */

@Api(tags = "验证码管理")
@RestController
@RequestMapping("/code")
public class VerificationCodeController {


    @Autowired
    private DefaultKaptcha captchaProducer;


    @ApiOperation("获取验证码图片")
    @GetMapping("/getVerificationCodePhoto")
    public void getVerificationCodePhoto(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        byte[] captchaOutputStream = null;
        ByteArrayOutputStream imgOutputStream = new ByteArrayOutputStream();
        try {
            //生成验证码
            String verifyCode = captchaProducer.createText();
            //验证码字符串保存到session中
            httpServletRequest.getSession().setAttribute("verifyCode", verifyCode);
            BufferedImage challenge = captchaProducer.createImage(verifyCode);
            //设置写出图片的格式
            ImageIO.write(challenge, "jpg", imgOutputStream);
        } catch (IllegalArgumentException e) {
            httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        captchaOutputStream = imgOutputStream.toByteArray();
        httpServletResponse.setHeader("Cache-Control", "no-store");
        httpServletResponse.setHeader("Pragma", "no-cache");
        httpServletResponse.setDateHeader("Expires", 0);
        httpServletResponse.setContentType("image/jpeg");
        ServletOutputStream responseOutputStream = httpServletResponse.getOutputStream();
        responseOutputStream.write(captchaOutputStream);
        responseOutputStream.flush();
        responseOutputStream.close();
    }


    @Log
    @ApiOperation("获取验证码")
    @GetMapping("/getVerificationCode")
    public R getVerificationCode(HttpServletRequest request) {
        R result = new R();
        String verifyCode = captchaProducer.createText();
        request.getSession().setAttribute("verifyCode", verifyCode);
        result.setData(verifyCode);
        return result;
    }
}
