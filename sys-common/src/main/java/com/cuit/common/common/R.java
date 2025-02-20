package com.cuit.common.common;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 通用返回结果，服务端响应的数据最终都会封装成此对象
 * @param <T>
 */
@Data
@ApiModel("返回实体类")
public class R<T> {

    @ApiModelProperty("返回代码")
    private String code; //编码：1成功，0和其它数字为失败

    @ApiModelProperty("返回信息")
    private String msg; //错误信息

    @ApiModelProperty("返回数据")
    private T data; //数据

    @ApiModelProperty("动态返回数据")
    private Map map = new HashMap(); //动态数据

    public static <T> R<T> success(T object) {
        R<T> r = new R<T>();
        r.data = object;
        r.code = CommonConstants.ConstantsCode.成功.getValue();
        return r;
    }

    public static <T> R<T> success(String msg) {
        R<T> r = new R<T>();
        r.msg = msg;
        r.code = CommonConstants.ConstantsCode.成功.getValue();
        return r;
    }

    public static <T> R<T> error(String msg) {
        R r = new R();
        r.msg = msg;
        r.code = CommonConstants.ConstantsCode.错误.getValue();
        return r;
    }

    public static R error(String code, String message) {
        R r = new R();
        r.msg = message;
        r.code = code;
        return r;
    }

    public static <T> R<T> success(String msg, T Object) {
        R<T> r = new R<T>();
        r.msg = msg;
        r.code = CommonConstants.ConstantsCode.成功.getValue();
        r.data=Object;
        return r;
    }

    public R<T> add(String key, Object value) {
        this.map.put(key, value);
        return this;
    }

}
