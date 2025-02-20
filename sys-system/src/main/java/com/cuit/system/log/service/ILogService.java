package com.cuit.system.log.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.cuit.system.log.domain.Log;

public interface ILogService extends IService<Log> {
    void removeAll();
}
