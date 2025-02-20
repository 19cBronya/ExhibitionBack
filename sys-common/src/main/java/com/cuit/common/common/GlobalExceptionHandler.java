package com.cuit.common.common;


import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.ShiroException;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.apache.shiro.authc.AuthenticationException;

import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理
 */
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 异常处理方法
     * @return
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(Exception ex){
        log.error(ex.getMessage());

        if(ex.getMessage().contains("Duplicate entry")){
            String[] split = ex.getMessage().split(" ");
            String msg = split[2] + "已存在";
            return R.error(msg);
        }

        return R.error("未知错误");
    }

    @ExceptionHandler(CommonException.class)
    public R<String> exceptionHandler(CommonException ex){
        log.error(ex.getMessage());
        return R.error(ex.getMessage());
    }

    /**
     * @author xym
     * @since 2023-11-07 11:23
     * @description
     */
    // 捕捉shiro的异常
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(ShiroException.class)
    public R handle401(ShiroException e) {
        return R.error(CommonConstants.ConstantsCode.错误.getValue(), e.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthenticationException.class)
    public R handleToken(ShiroException e) {
        return R.error(CommonConstants.ConstantsCode.错误.getValue(),"身份验证失败");
    }

    // 捕捉shiro没有权限的异常
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    public R handle401(UnauthorizedException e) {
        System.out.println(e.getMessage());
        return R.error(CommonConstants.ConstantsCode.错误.getValue(), "你没有权限访问");
    }
    // 捕捉shiro没有认证的异常
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthenticatedException.class)
    public R handle401(UnauthenticatedException e) {
        System.out.println(e.getMessage());
        return R.error(CommonConstants.ConstantsCode.错误.getValue(), "你没有认证");
    }

    /**
     * @Validated 校验错误异常处理
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public R handler(MethodArgumentNotValidException e) throws IOException {
        log.error("运行时异常:-------------->",e);
        BindingResult bindingR = e.getBindingResult();
        //这一步是把异常的信息最简化
        ObjectError objectError = bindingR.getAllErrors().stream().findFirst().get();
        return R.error(String.valueOf(HttpStatus.BAD_REQUEST.value()),objectError.getDefaultMessage());
    }

    //运行时错误处理
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = RuntimeException.class)
    public R handle(RuntimeException e){
        return R.error(String.valueOf(HttpStatus.BAD_REQUEST.value()),e.getMessage());
    }



}
