package com.cuit.common.common;

/**
 * 自定义业务异常类
 */
public class CommonException extends RuntimeException {
    public CommonException(String message){
        super(message);
    }
}
