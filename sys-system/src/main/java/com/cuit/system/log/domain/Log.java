package com.cuit.system.log.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Log implements Serializable {

    private Long id;
    /*操作者ID*/
    private Long uid;
    /*操作者姓名*/
    private String uName;
    /*对应类名*/
    private String className;
    /*调用方法*/
    private String methodName;
    /*方法参数*/
    private String methodParams;
    /*返回值*/
    private String returnValues;
    /*花费时间*/
    private Long costTime;
    /*删除标志 0正常 1删除*/
    private String delFlag;
    /*创建时间*/
    private LocalDateTime createTime;
}
