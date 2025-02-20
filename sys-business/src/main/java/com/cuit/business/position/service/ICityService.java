package com.cuit.business.position.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cuit.business.position.domain.City;
import com.cuit.business.position.domain.dto.PositionDTO;

import java.util.List;

public interface ICityService extends IService<City> {
    List<PositionDTO> selectCityList(Integer id);
}
