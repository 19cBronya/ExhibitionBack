package com.cuit.business.position.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cuit.business.position.domain.CityProper;
import com.cuit.business.position.domain.dto.PositionDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CityProperMapper extends BaseMapper<CityProper> {
    List<PositionDTO> selectCityProperList(Integer id);
}
