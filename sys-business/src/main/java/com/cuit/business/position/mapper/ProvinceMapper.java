package com.cuit.business.position.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cuit.business.position.domain.Province;
import com.cuit.business.position.domain.dto.PositionDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProvinceMapper extends BaseMapper<Province> {
    List<PositionDTO> selectProcinceList();
}
