package com.cuit.business.position.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cuit.business.position.domain.CityProper;
import com.cuit.business.position.domain.dto.PositionDTO;

import java.util.List;

public interface ICityProperService extends IService<CityProper> {
    List<PositionDTO> selectCityProperList(Integer id);
}
