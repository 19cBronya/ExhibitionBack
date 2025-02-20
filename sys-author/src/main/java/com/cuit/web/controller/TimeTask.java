package com.cuit.web.controller;

import com.cuit.system.log.service.ILogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Component
public class TimeTask {
    @Autowired
    private ILogService logService;

    @Scheduled(cron = "0 * * * * ?") // 每间隔一分钟开启一次
    public void clearDataByDesgin() {
        log.info("---------定时任务开始执行！---------" + new SimpleDateFormat("HH:mm:ss").format(new Date()));
        // 获取当前数据条数
        int logCount = logService.count();
        // 判断数据是否足够50条，如果不足则不执行删除
        if (logCount >= 50) {
            clearData();
            log.info("---------定时任务执行成功！---------" + new SimpleDateFormat("HH:mm:ss").format(new Date()));
        } else {
            log.info("---------数据条数不足50条，不执行删除操作---------");
        }
    }
    private void clearData() {
        try {
            logService.removeAll();
        } catch (Exception e) {
            log.error("清理数据失败，失败原因：" + e.getMessage());
        }
    }
}

