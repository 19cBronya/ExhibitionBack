package com.cuit.system.log.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cuit.system.log.domain.Log;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LogMapper  extends BaseMapper<Log> {
    void removeAll();
}
