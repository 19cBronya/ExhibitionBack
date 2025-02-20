package com.cuit.business.booth.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cuit.business.booth.domain.Booth;
import com.cuit.business.booth.domain.BoothReservation;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface BoothMapper extends BaseMapper<Booth> {
    List<Booth> selectListByEid(Long id);

    List<Booth> selectListByEidStatus(Long id);

}
