package com.cuit.business.position.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cuit.business.position.domain.City;
import com.cuit.business.position.domain.dto.PositionDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CityMapper extends BaseMapper<City> {
    List<PositionDTO> selectCityList(Integer id);
}
