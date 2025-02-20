package com.cuit.system.log.aop;


import com.alibaba.fastjson.JSONObject;
import com.cuit.common.common.CommonConstants;
import com.cuit.system.log.domain.Log;
import com.cuit.system.log.mapper.LogMapper;
import com.cuit.system.sysUser.domain.SysUser;
import org.apache.shiro.SecurityUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

@Component
@Aspect
public class LogAspect {
    @Autowired
    private LogMapper logMapper;
    @Around("@annotation(com.cuit.system.log.anno.Log)")
    public Object recordLog(ProceedingJoinPoint joinPoint) throws Throwable {
        SysUser sysUser = (SysUser) SecurityUtils.getSubject().getPrincipal();

        String uName=null;
        Long uid =null;
        if(sysUser!=null){
            uid = sysUser.getId();
            uName = sysUser.getName();
        }


        LocalDateTime time = LocalDateTime.now();

        String className = joinPoint.getTarget().getClass().getName();

        String methodName = joinPoint.getSignature().getName();

        Object[] agrs = joinPoint.getArgs();
        String methodParams = Arrays.toString(agrs);

        long begin = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long end = System.currentTimeMillis();

        String returnValues = JSONObject.toJSONString(result);

        Long costTime = end -begin;

        String delFlag = CommonConstants.DeleteFlag.正常.getValue();

        Log log =new Log(null,uid,uName,className,methodName,methodParams,returnValues,costTime,delFlag,time);
        logMapper.insert(log);
        return result;
    }
}
