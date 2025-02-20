package com.cuit.system.log.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cuit.system.log.domain.Log;
import com.cuit.system.log.mapper.LogMapper;
import com.cuit.system.log.service.ILogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ILogServiceImpl extends ServiceImpl<LogMapper, Log> implements ILogService {

    @Autowired
    private LogMapper logMapper;
    @Override
    public void removeAll() {
        logMapper.removeAll();
    }
}
